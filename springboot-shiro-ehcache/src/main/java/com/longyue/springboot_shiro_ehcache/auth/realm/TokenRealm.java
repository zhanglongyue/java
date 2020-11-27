package com.longyue.springboot_shiro_ehcache.auth.realm;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.longyue.springboot_shiro_ehcache.domain.Menu;
import com.longyue.springboot_shiro_ehcache.domain.Role;
import com.longyue.springboot_shiro_ehcache.domain.User;
import com.longyue.springboot_shiro_ehcache.service.UserService;
import com.longyue.springboot_shiro_ehcache.auth.token.UserToken;
import com.longyue.springboot_shiro_ehcache.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class TokenRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UserToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.info("Shiro权限配置");
        String token = principals.toString();
        User user = null;
        try {
            user = JSON.parseObject(RedisUtils.StringOps.get(token), User.class);
        } catch (Exception e) {
            return null;
        }
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        for (Role role:user.getRoles()) {
            //添加角色
            simpleAuthorizationInfo.addRole(role.getName());
            for (Menu menu:role.getMenus()) {
                //添加权限
                String permission = menu.getPermission();
                if(StrUtil.isNotBlank(permission)) {
                    simpleAuthorizationInfo.addStringPermission(menu.getPermission());
                }
            }
        }
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        log.info("Shiror认证");
        UserToken userToken = (UserToken) token;
        String defaultToken = (String) userToken.getCredentials();
        if (StrUtil.isBlank(defaultToken)) {
            return null;
        }
        log.info("token: " + token);
        return new SimpleAccount(defaultToken, defaultToken, this.getName());
    }
}
