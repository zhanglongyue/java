package priv.yue.sboot.auth.filter;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import priv.yue.sboot.auth.token.DefaultToken;
import priv.yue.sboot.common.constant.Consts;
import priv.yue.sboot.utils.RedisUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TokenFilter extends DefaultFilter {

    private static final String AUTHORIZATION = "Authorization";

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader(AUTHORIZATION);
        if (StrUtil.isBlank(token)) {
            return false;
        }
        if (!RedisUtils.KeyOps.hasKey(Consts.SHIRO_TOKEN_PREFIX + token)){
            return false;
        }
        //刷新超时时间
        RedisUtils.KeyOps.expire(Consts.SHIRO_TOKEN_PREFIX + token,30, TimeUnit.MINUTES);
        getSubject(request, response).login(new DefaultToken(token));
        return true;
    }

}
