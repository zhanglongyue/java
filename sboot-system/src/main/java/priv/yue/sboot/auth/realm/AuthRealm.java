package priv.yue.sboot.auth.realm;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.shiro.subject.PrincipalCollection;
import priv.yue.sboot.domain.Menu;
import priv.yue.sboot.domain.Role;
import priv.yue.sboot.domain.User;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import priv.yue.sboot.domain.vo.LoginVo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AuthRealm extends AuthorizingRealm {

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return getAuthorizationByUser((LoginVo) principals.getPrimaryPrincipal());
    }

    /**
     * 根据登录信息给realm设置角色权限
     * @param loginVo 包含用户角色权限的登录信息
     * @return AuthorizationInfo
     */
    protected AuthorizationInfo getAuthorizationByUser(LoginVo loginVo){
        return getAuthorization(loginVo.getRoles().stream().map(Role::getName).collect(Collectors.toSet()),
                                loginVo.getMenus().stream().map(Menu::getName).collect(Collectors.toSet()));
    }


    protected AuthorizationInfo getAuthorization(List<String> roles, List<String> menus){
        return getAuthorization(new HashSet<>(roles), new HashSet<>(menus));
    }

    protected AuthorizationInfo getAuthorization(Set<String> roles, Set<String> menus){
        if (ObjectUtil.isEmpty(roles) && ObjectUtil.isEmpty(menus)){
            return null;
        }
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setRoles(roles);
        simpleAuthorizationInfo.setStringPermissions(menus);
        return simpleAuthorizationInfo;
    }
}
