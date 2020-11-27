package com.longyue.springboot_shiro_ehcache.auth.realm;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.longyue.springboot_shiro_ehcache.domain.Menu;
import com.longyue.springboot_shiro_ehcache.domain.Role;
import com.longyue.springboot_shiro_ehcache.domain.User;
import com.longyue.springboot_shiro_ehcache.service.UserService;
import com.longyue.springboot_shiro_ehcache.utils.SimpleByteSourceSerializable;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //获取登录用户名
        String username = (String) principals.getPrimaryPrincipal();
        //查询用户名称
        User user = userService.getUserByName(username);
        //添加角色和权限
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
