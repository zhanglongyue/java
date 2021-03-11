package priv.yue.auth.realm;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.springframework.util.ObjectUtils;
import priv.yue.auth.core.realm.AuthRealm;
import priv.yue.auth.service.AuthService;
import priv.yue.auth.utils.SimpleByteSourceSerializable;
import priv.yue.common.domain.User;
import priv.yue.common.vo.LoginVo;

import javax.annotation.Resource;

@Slf4j
public class UserRealm extends AuthRealm {

    @Resource
    private AuthService authService;

    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        if (token.getPrincipal() == null) {
            return null;
        }
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        String username = token.getPrincipal().toString();
        LoginVo loginVo = authService.getLoginUserByName(username);
        loginVo.setRememberMe(usernamePasswordToken.isRememberMe());
        User user = loginVo.getUser();
        if (ObjectUtils.isEmpty(user)) {
            return null;
        } else {
            // 验证authenticationToken和simpleAuthenticationInfo的信息
            return new SimpleAuthenticationInfo(loginVo, user.getPassword(),
                    SimpleByteSourceSerializable.Util.bytes(user.getSalt()), getName());
        }
    }
}
