package priv.yue.sboot.realm;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import priv.yue.sboot.auth.GetAuthorizationAble;

import java.util.HashSet;
import java.util.Set;

/**
 * 该类完成realm的获取授权信息，实现其doGetAuthorizationInfo方法
 */
@Slf4j
public abstract class AuthRealm extends AuthorizingRealm {

    /**
     * shiro通过该方法获取用户权限信息
     * @param principals 该参数为doGetAuthenticationInfo方法中返回的认证信息，即用户信息
     * @return 授权信息
     */
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Object p = principals.getPrimaryPrincipal();
        if (p instanceof GetAuthorizationAble) {
            return getAuthorization((GetAuthorizationAble) p);
        }
        return null;
    }

    protected AuthorizationInfo getAuthorization(GetAuthorizationAble principals){
        return getAuthorization(new HashSet<>(principals.getRolesStr()),
                                new HashSet<>(principals.getPermissionsStr()));
    }

    protected AuthorizationInfo getAuthorization(Set<String> roles, Set<String> Permissions){
        if (ObjectUtil.isEmpty(roles) && ObjectUtil.isEmpty(Permissions)){
            return null;
        }
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setRoles(roles);
        simpleAuthorizationInfo.setStringPermissions(Permissions);
        return simpleAuthorizationInfo;
    }

}
