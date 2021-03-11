package priv.yue.auth.config;

import lombok.Setter;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.realm.Realm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import priv.yue.auth.realm.UserRealm;

/**
 * @author  ZhangLongYue
 * @date  ${DATE} ${TIME}
 * @version 1.0
 */
@Configuration
@Setter
public class RealmConfig {

    /**
     * 注意这个realm需要在tokenRealm加载后加载，顺序不能改变，会影响spring封装List顺序
     * 优先使用tokenRealm从redis中获取认证及授权信息，多realm时，shiro会按顺序进行认证，优先使用token
     * @param credentialsMatcher
     * @param cacheManager
     * @return
     */
    @Bean
    @DependsOn("tokenRealm")
    public Realm userRealm(CredentialsMatcher credentialsMatcher, CacheManager cacheManager){
        UserRealm realm = new UserRealm();
        realm.setCredentialsMatcher(credentialsMatcher);
        // 开启缓存管理器 使用redis时无需以下配置
        /*realm.setCacheManager(cacheManager);
        realm.setCachingEnabled(true);
        realm.setAuthenticationCachingEnabled(true);
        realm.setAuthenticationCacheName("authenticationCache");
        realm.setAuthorizationCachingEnabled(true);
        realm.setAuthorizationCacheName("authorizationCache");*/
        return realm;
    }

}