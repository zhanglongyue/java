package priv.yue.sboot.auth.realm;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import priv.yue.sboot.domain.User;
import priv.yue.sboot.vo.LoginVo;
import priv.yue.sboot.service.UserService;
import priv.yue.sboot.utils.SimpleByteSourceSerializable;

@Slf4j
public class UserRealm extends AuthRealm {

    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    @Autowired
    private UserService userService;

    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        if (token.getPrincipal() == null) {
            return null;
        }
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        String username = token.getPrincipal().toString();
        LoginVo loginVo = userService.getLoginUserByName(username);
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
