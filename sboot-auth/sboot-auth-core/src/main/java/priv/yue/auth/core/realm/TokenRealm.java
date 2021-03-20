package priv.yue.auth.core.realm;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;
import org.springframework.beans.factory.annotation.Autowired;
import priv.yue.auth.core.config.AuthConfig;
import priv.yue.auth.core.constant.Constants;
import priv.yue.auth.core.token.SimpleToken;
import priv.yue.common.utils.JsonUtils;
import priv.yue.common.utils.RedisUtils;
import priv.yue.common.vo.LoginVo;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TokenRealm extends AuthRealm {

    @Resource
    private AuthConfig authConfig;

    public boolean supports(AuthenticationToken token) {
        return token instanceof SimpleToken;
    }

    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        SimpleToken simpleToken = (SimpleToken) token;
        String userToken = (String) simpleToken.getPrincipal();
        LoginVo loginVo = null;
        try {
            loginVo = JsonUtils.jsonToBean(RedisUtils.StringOps.get(Constants.SHIRO_TOKEN_PREFIX + userToken), LoginVo.class);
        } catch (Exception e) {
            return null;
        }
        if (ObjectUtil.isEmpty(loginVo)) {
            return null;
        }
        //刷新超时时间
        Long tokenTimeout = loginVo.getRememberMe() ? authConfig.getRemeberMeTimeout()
                : authConfig.getDefaultTokenTimeout();
        RedisUtils.KeyOps.expire(Constants.SHIRO_TOKEN_PREFIX + userToken, tokenTimeout, TimeUnit.MILLISECONDS);
        return new SimpleAccount(loginVo, userToken, this.getName());
    }
}
