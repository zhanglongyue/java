package com.longyue.springboot_shiro_ehcache.auth.realm;

import cn.hutool.core.util.StrUtil;
import com.longyue.springboot_shiro_ehcache.service.UserService;
import com.longyue.springboot_shiro_ehcache.auth.token.UserToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authz.AuthorizationInfo;
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
//        log.info("Shiro权限配置");
//        String token = principals.toString();
//
//        UserDetailVO userDetailVO = JSON.parseObject(jedis.get(token), UserDetailVO.class);
//
//        Set<String> roles = new HashSet<>();
//        roles.add(userDetailVO.getAuthType() + "");
//        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//        info.setRoles(roles);
//        return info;
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        log.info("Shiror认证");
        UserToken userToken = (UserToken) token;
        //获取用户的输入的账号.
        String sid = (String) userToken.getCredentials();
        if (StrUtil.isBlank(sid)) {
            return null;
        }
        log.info("sid: " + sid);
        return new SimpleAccount(sid, sid, this.getName());
    }
}
