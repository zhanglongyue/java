package com.longyue.springboot_shiro_ehcache.auth.realm;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.longyue.springboot_shiro_ehcache.domain.Menu;
import com.longyue.springboot_shiro_ehcache.domain.Role;
import com.longyue.springboot_shiro_ehcache.domain.User;
import com.longyue.springboot_shiro_ehcache.service.UserService;
import com.longyue.springboot_shiro_ehcache.utils.RedisUtils;
import com.longyue.springboot_shiro_ehcache.utils.SimpleByteSourceSerializable;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import java.util.stream.Collectors;

public class UserRealm extends AuthRealm {

    @Autowired
    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //获取登录用户名
        String username = (String) principals.getPrimaryPrincipal();
        //调用父类方法设置角色和权限
        return setRolesAndPermissions(userService.getUserByName(username));
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        if (token.getPrincipal() == null) {
            return null;
        }
        String username = token.getPrincipal().toString();
        User user = userService.getUserByName(username);
        if (ObjectUtils.isEmpty(user)) {
            return null;
        } else {
            //这里验证authenticationToken和simpleAuthenticationInfo的信息
            return new SimpleAuthenticationInfo(user, user.getPassword(),
                    SimpleByteSourceSerializable.Util.bytes(user.getSalt()), getName());
        }
    }
}
