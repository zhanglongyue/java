package priv.yue.sboot.auth.filter;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import priv.yue.sboot.auth.token.DefaultToken;
import priv.yue.sboot.common.Consts;
import priv.yue.sboot.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TokenFilter extends DefaultFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader("token");
        if (StringUtils.isBlank(token)) {
            return false;
        }
        if (!RedisUtils.KeyOps.hasKey(Consts.TOKEN_PREFIX + token)){
            return false;
        }
        //刷新超时时间
        RedisUtils.KeyOps.expire(Consts.TOKEN_PREFIX + token,30, TimeUnit.MINUTES);
        getSubject(request, response).login(new DefaultToken(token));
        return true;
    }

}
