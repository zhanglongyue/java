package priv.yue.sboot.auth.realm;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;
import org.springframework.beans.factory.annotation.Autowired;
import priv.yue.sboot.auth.token.DefaultToken;
import priv.yue.sboot.common.constant.Consts;
import priv.yue.sboot.config.SbootConfig;
import priv.yue.sboot.vo.LoginVo;
import priv.yue.sboot.utils.JsonUtils;
import priv.yue.sboot.utils.RedisUtils;

import java.util.concurrent.TimeUnit;

@Slf4j
public class TokenRealm extends AuthRealm {

    @Autowired
    private SbootConfig sbootConfig;

    public boolean supports(AuthenticationToken token) {
        return token instanceof DefaultToken;
    }

    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        DefaultToken defaultToken = (DefaultToken) token;
        String userToken = (String) defaultToken.getPrincipal();
        LoginVo loginVo = null;
        try {
            loginVo = JsonUtils.toObject(RedisUtils.StringOps.get(Consts.SHIRO_TOKEN_PREFIX + userToken), LoginVo.class);
        } catch (Exception e) {
            return null;
        }
        if (ObjectUtil.isEmpty(loginVo)) {
            return null;
        }
        //刷新超时时间
        Long tokenTimeout = loginVo.getRememberMe() ? sbootConfig.getRemeberMeTimeout()
                : sbootConfig.getDefaultTokenTimeout();
        RedisUtils.KeyOps.expire(Consts.SHIRO_TOKEN_PREFIX + userToken, tokenTimeout, TimeUnit.MILLISECONDS);
        return new SimpleAccount(loginVo, userToken, this.getName());
    }
}
