package com.longyue.springboot_shiro_ehcache.auth.pam;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;

import java.util.Collection;

public class DefaultModularRealmAuthenticator extends ModularRealmAuthenticator {
    /**
     * 管理多token和多realm，在使用subject.login()时，不同的token将使用不同的realm
     * UsernamePasswordToken将使用UserRealm查询数据库获取用户信息，在调用login方法时使用
     * DefaultToken将使用TokenRealm获取用户信息
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doAuthenticate(AuthenticationToken authenticationToken) throws AuthenticationException {
        assertRealmsConfigured();
        Collection<Realm> realms = getRealms();
        if (realms.size() == 1) {
            return doSingleRealmAuthentication(realms.iterator().next(), authenticationToken);
        } else {
            /**
             * 判断realm是否支持该token，如果支持，使用该realm进行认证
             */
            for (Realm realm : realms) {
                if(realm.supports(authenticationToken)){
                    return doSingleRealmAuthentication(realm, authenticationToken);
                }
            }
            return doMultiRealmAuthentication(realms, authenticationToken);
        }
    }
}
