package com.longyue.springboot_shiro_ehcache.auth.realm;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.longyue.springboot_shiro_ehcache.domain.User;
import com.longyue.springboot_shiro_ehcache.service.UserService;
import com.longyue.springboot_shiro_ehcache.auth.token.DefaultToken;
import com.longyue.springboot_shiro_ehcache.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class TokenRealm extends AuthRealm {

    @Autowired
    private UserService userService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof DefaultToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String token = principals.toString();
        return setRolesAndPermissions(JSON.parseObject(RedisUtils.StringOps.get(token), User.class));
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        DefaultToken defaultToken = (DefaultToken) token;
        String userToken = (String) defaultToken.getCredentials();
        if (StrUtil.isBlank(userToken)) {
            return null;
        }
        return new SimpleAccount(userToken, userToken, this.getName());
    }
}
