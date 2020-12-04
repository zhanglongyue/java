package priv.yue.sboot.auth.realm;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import priv.yue.sboot.domain.Menu;
import priv.yue.sboot.domain.User;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;

import java.util.stream.Collectors;

public abstract class AuthRealm extends AuthorizingRealm {
    /**
     * 根据用户信息给realm设置角色权限
     * @param user 包含角色及权限的用户对象
     * @return AuthorizationInfo
     */
    protected AuthorizationInfo getAuthorizationInfoByUser(User user){
        if (ObjectUtil.isEmpty(user)){
            return null;
        }
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        user.getRoles().forEach(role -> {
            simpleAuthorizationInfo.addRole(role.getName());
            simpleAuthorizationInfo.addStringPermissions(
                    role.getMenus().stream().filter(menu -> StrUtil.isNotBlank(menu.getPermission()))
                            .map(Menu::getPermission)
                            .collect(Collectors.toSet())
            );
        });
        return simpleAuthorizationInfo;
    }
}
