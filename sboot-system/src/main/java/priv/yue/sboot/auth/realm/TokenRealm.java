package priv.yue.sboot.auth.realm;

import cn.hutool.core.util.ObjectUtil;
import priv.yue.sboot.auth.token.DefaultToken;
import priv.yue.sboot.common.Consts;
import priv.yue.sboot.domain.User;
import priv.yue.sboot.domain.vo.LoginVo;
import priv.yue.sboot.utils.JsonUtils;
import priv.yue.sboot.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;

@Slf4j
public class TokenRealm extends AuthRealm {

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof DefaultToken;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        DefaultToken defaultToken = (DefaultToken) token;
        String userToken = (String) defaultToken.getPrincipal();
        LoginVo loginVo = null;
        try {
            loginVo = JsonUtils.toObject(RedisUtils.StringOps.get(Consts.TOKEN_PREFIX + userToken), User.class);
        } catch (Exception e) {
            return null;
        }
        if (ObjectUtil.isEmpty(loginVo)) {
            return null;
        }
        return new SimpleAccount(loginVo.getUser(), userToken, this.getName());
    }
}
