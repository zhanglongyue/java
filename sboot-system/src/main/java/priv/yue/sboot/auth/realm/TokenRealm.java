package priv.yue.sboot.auth.realm;

import cn.hutool.core.util.ObjectUtil;
import priv.yue.sboot.auth.token.DefaultToken;
import priv.yue.sboot.common.Consts;
import priv.yue.sboot.domain.User;
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
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return getAuthorizationInfoByUser((User) principals.getPrimaryPrincipal());
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        DefaultToken defaultToken = (DefaultToken) token;
        String userToken = (String) defaultToken.getPrincipal();
        User user = null;
        try {
            user = JsonUtils.toObject(RedisUtils.StringOps.get(Consts.TOKEN_PREFIX + userToken), User.class);
        } catch (Exception e) {
            return null;
        }
        if (ObjectUtil.isEmpty(user)) {
            return null;
        }
        return new SimpleAccount(user, userToken, this.getName());
    }
}
