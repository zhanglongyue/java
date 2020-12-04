package priv.yue.sboot.auth.realm;

import priv.yue.sboot.domain.User;
import priv.yue.sboot.service.UserService;
import priv.yue.sboot.utils.SimpleByteSourceSerializable;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

@Slf4j
public class UserRealm extends AuthRealm {

    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return getAuthorizationInfoByUser((User) principals.getPrimaryPrincipal());
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
            // 验证authenticationToken和simpleAuthenticationInfo的信息
            return new SimpleAuthenticationInfo(user, user.getPassword(),
                    SimpleByteSourceSerializable.Util.bytes(user.getSalt()), getName());
        }
    }
}
