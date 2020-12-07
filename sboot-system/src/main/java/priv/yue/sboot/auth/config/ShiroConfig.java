package priv.yue.sboot.auth.config;

import lombok.Setter;
import priv.yue.sboot.auth.filter.TokenFilter;
import priv.yue.sboot.auth.pam.DefaultModularRealmAuthenticator;
import priv.yue.sboot.auth.realm.TokenRealm;
import priv.yue.sboot.auth.realm.UserRealm;
import lombok.Data;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "shiro")
@Setter
public class ShiroConfig {

    private String loginUrl;
    private String unauthorizedUrl;
    private String successUrl;
    private String logoutUrl;

    private String[] anons;
    private String[] authcs;

    private String hashAlgorithm;
    private Integer hashIterations;

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
        filters.put("authc", new TokenFilter());

        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl(loginUrl);
        shiroFilterFactoryBean.setUnauthorizedUrl(unauthorizedUrl);
        shiroFilterFactoryBean.setSuccessUrl(successUrl);

        Map<String,String> filterMap = new HashMap<>();
        if(null != logoutUrl){
            filterMap.put(logoutUrl, "logout");
        }
        if(anons!=null && anons.length>0){
            for(String anon:anons){
                filterMap.put(anon, "anon");
            }
        }
        if(authcs!=null && authcs.length>0){
            for(String authc:authcs){
                filterMap.put(authc, "authc");
            }
        }

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
        return shiroFilterFactoryBean;
    }

    /**
     * 配置SecurityManager
     * 注意这里的List<Realm>会由spring自动封装，spring支持将接口的多实现封装为List或Map
     * @param realms 自定义realm
     * @return SecurityManager
     */
    @Bean
    public SecurityManager securityManager(List<Realm> realms, ModularRealmAuthenticator modularRealmAuthenticator,
                                           @Qualifier("sessionManager") SessionManager sessionManager,
                                           CacheManager cacheManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setAuthenticator(modularRealmAuthenticator);
        securityManager.setRealms(realms);

//        securityManager.setSessionManager(sessionManager);
//        securityManager.setCacheManager(cacheManager);

        /*
         * 关闭shiro自带的session，详情见文档
         * http://shiro.apache.org/session-management.html#SessionManagement-StatelessApplications%28Sessionless%29
         */
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
        return securityManager;
    }

    /**
     * 自定义Realm管理，针对多realm
     * */
    @Bean
    public ModularRealmAuthenticator modularRealmAuthenticator(){
        DefaultModularRealmAuthenticator modularRealmAuthenticator = new DefaultModularRealmAuthenticator();
        modularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        return modularRealmAuthenticator;
    }

    /**
     * TokenRealm
     * 注意这2个realm顺序不能改变，会影响spring封装List顺序
     * 优先使用tokenRealm从redis中获取认证及授权信息
     */
    @Bean
    public Realm tokenRealm() {
        return new TokenRealm();
    }
    /**
     * 用户名密码Realm
     */
    @Bean
    public Realm userRealm(CredentialsMatcher credentialsMatcher, CacheManager cacheManager){
        UserRealm realm = new UserRealm();
        realm.setCredentialsMatcher(credentialsMatcher);
        // 开启缓存管理器
//        realm.setCacheManager(cacheManager);
//        realm.setCachingEnabled(true);
//        realm.setAuthenticationCachingEnabled(true);
//        realm.setAuthenticationCacheName("authenticationCache");
//        realm.setAuthorizationCachingEnabled(true);
//        realm.setAuthorizationCacheName("authorizationCache");
        return realm;
    }

    /**
     * 加dependson解决热部署 重复Bean问题
     * @return CacheManager
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public CacheManager cacheManager() {
        return new EhCacheManager();
    }

    /**
     * 配置凭证匹配器,用于做密码散列
     * @return CredentialsMatcher
     */
    @Bean
    public CredentialsMatcher credentialsMatcher() {
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        credentialsMatcher.setHashAlgorithmName(hashAlgorithm);
        credentialsMatcher.setHashIterations(hashIterations);
        return credentialsMatcher;
    }

    /**
     * 处理第一次访问jsessionid 400
     * @return
     */
    @Bean
    public SessionManager sessionManager(){
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        return sessionManager;
    }

    /**
     * 解决热部署 cacheManager重复Bean问题
     */
    @Bean(name = "lifecycleBeanPostProcessor")
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }


    /**
     * 以下配置开启shiro注解
     * springboot在2.X后默认使用cglib代理，不需要开启以下配置，否则会导致二次代理
     */
//    @Bean
//    public static DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
//        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
//        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
//        /**
//         * setUsePrefix(false)用于解决一个奇怪的bug。在引入spring aop的情况下。
//         * 在@Controller注解的类的方法中加入@RequiresRole等shiro注解，会导致该方法无法映射请求，导致返回404。
//         * 加入这项配置能解决这个bug
//         */
//        // defaultAdvisorAutoProxyCreator.setUsePrefix(true);
//        return defaultAdvisorAutoProxyCreator;
//    }
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }



    // ****************** 使用redis管理session与cache ****************** //
    // 使用时需要在securityManager中装配

    /**
     * cacheManager 缓存 redis实现
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    @Bean
    public CacheManager redisCacheManager(RedisManager redisManager) {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager);
        return redisCacheManager;
    }

    /**
     * 配置shiro redisManager
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    @Bean
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost("localhost:6379");
        // redisManager.setPassword(password);
        return redisManager;
    }

    /**
     * Session Manager
     * 使用的是shiro-redis开源插件
     */
    @Bean
    public SessionManager redisSessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        sessionManager.setSessionDAO(redisSessionDAO());
        return sessionManager;
    }

    /**
     * RedisSessionDAO shiro sessionDao层的实现 通过redis
     * 使用的是shiro-redis开源插件
     */
    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        return redisSessionDAO;
    }

}