package com.longyue.springboot_shiro_ehcache.auth.realm;

import com.longyue.springboot_shiro_ehcache.service.UserService;
import com.longyue.springboot_shiro_ehcache.auth.token.JwtToken;
import com.longyue.springboot_shiro_ehcache.utils.JwtUtils;
import com.longyue.springboot_shiro_ehcache.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class JwtRealm extends AuthorizingRealm {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private UserService userService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        return null;
//        String jwt = (String) token.getPrincipal();
//        if (jwt == null) {
//            throw new NullPointerException("jwtToken 不允许为空");
//        }
//        //判断
////        if (!jwtUtil.isVerify(jwt)) {
////            throw new UnknownAccountException();
////        }
////        //下面是验证这个user是否是真实存在的
////        String username = (String) jwtUtil.decode(jwt).get("username");//判断数据库中username是否存在
////        log.info("在使用token登录"+username);
//        return new SimpleAuthenticationInfo(jwt,jwt,getName());
//        //这里返回的是类似账号密码的东西，但是jwtToken都是jwt字符串。还需要一个该Realm的类名

    }

}
