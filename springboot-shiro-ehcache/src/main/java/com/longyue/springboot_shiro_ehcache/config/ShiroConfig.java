package com.longyue.springboot_shiro_ehcache.config;

import com.longyue.springboot_shiro_ehcache.realm.UserRealm;
import lombok.Data;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "shiro")
@Data
public class ShiroConfig {

    private String loginUrl;
    private String unauthorizedUrl;
    private String successUrl;
    private String logoutUrl;

    private String[] anons;
    private String[] authcs;

    private String hashAlgorithm;
    private Integer hashIterations;

    /**
     * 配置securityManager
     * @param realm
     * @return
     */
    @Bean
    public DefaultWebSecurityManager getSecurityManager(Realm realm){
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setRealm(realm);
        return defaultWebSecurityManager;
    }

    /**
     * 配置shiroFilter
     * @param securityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();

        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl(loginUrl);
        shiroFilterFactoryBean.setUnauthorizedUrl(unauthorizedUrl);
        shiroFilterFactoryBean.setSuccessUrl(successUrl);

        Map<String,String> filterMap = new HashMap<>();

        if(null != logoutUrl){
            filterMap.put(loginUrl,"logout");
        }
        if(anons!=null && anons.length>0){
            for(String anon:anons){
                filterMap.put(anon,"anon");
            }
        }
        if(authcs!=null && authcs.length>0){
            for(String authc:authcs){
                filterMap.put(authc,"authc");
            }
        }

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public EhCacheManager ehCacheManager(){
        return new EhCacheManager();
    }

    /**
     * 配置自定义Realm
     * @return
     */
    @Bean
    public Realm realm(CredentialsMatcher credentialsMatcher, EhCacheManager ehCacheManager){
        UserRealm userRealm = new UserRealm();
        userRealm.setCredentialsMatcher(credentialsMatcher);
        //开启缓存管理器
        userRealm.setCachingEnabled(true);
        userRealm.setAuthorizationCachingEnabled(true);
        userRealm.setAuthorizationCachingEnabled(true);
        userRealm.setCacheManager(ehCacheManager);
        return userRealm;
    }

    /**
     * 配置凭证匹配器
     * @return
     */
    @Bean
    public CredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        // 设置加密方式
        credentialsMatcher.setHashAlgorithmName(hashAlgorithm);
        // 设置散列次数
        credentialsMatcher.setHashIterations(hashIterations);
        return credentialsMatcher;
    }

}