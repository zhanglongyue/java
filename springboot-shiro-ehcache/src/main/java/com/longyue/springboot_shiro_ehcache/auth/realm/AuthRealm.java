package com.longyue.springboot_shiro_ehcache.auth.realm;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.longyue.springboot_shiro_ehcache.domain.User;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;

import java.util.stream.Collectors;

public abstract class AuthRealm extends AuthorizingRealm {
    /**
     * 根据用户信息给realm设置用户权限
     * @param user
     * @return
     */
    protected AuthorizationInfo setRolesAndPermissions(User user){
        if (ObjectUtil.isEmpty(user)){
            return null;
        }
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        user.getRoles().stream().forEach(role -> {
            simpleAuthorizationInfo.addRole(role.getName());
            simpleAuthorizationInfo.addStringPermissions(
                    role.getMenus().stream().filter(menu -> StrUtil.isNotBlank(menu.getPermission()))
                            .map(menu -> menu.getPermission())
                            .collect(Collectors.toSet())
            );
        });
        return simpleAuthorizationInfo;
    }
}
