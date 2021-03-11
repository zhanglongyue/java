package priv.yue.auth.core.filter;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import priv.yue.auth.core.constant.Constants;
import priv.yue.auth.core.token.SimpleToken;
import priv.yue.common.utils.RedisUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

@Slf4j
public class TokenFilter extends DefaultFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader(Constants.AUTHORIZATION);
        if (StrUtil.isBlank(token)) {
            return false;
        }
        if (!RedisUtils.KeyOps.hasKey(Constants.SHIRO_TOKEN_PREFIX + token)){
            return false;
        }
        getSubject(request, response).login(new SimpleToken(token));
        return true;
    }

}
