package com.longyue.springboot_shiro_ehcache.auth.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.longyue.springboot_shiro_ehcache.common.RestResponse;
import com.longyue.springboot_shiro_ehcache.domain.User;
import com.longyue.springboot_shiro_ehcache.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TokenFilter extends FormAuthenticationFilter {

    /**
     * 如果在这里返回了false，请求onAccessDenied（）
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader("token");
        if (StringUtils.isBlank(token)) {
            return false;
        }
        log.info("token: " + token);
        User user = null;
        try {
            user = JSON.parseObject(RedisUtil.StringOps.get(token), User.class);
        } catch (Exception e) {
            return false;
        }
        if (user == null) {
            return false;
        }
        //刷新超时时间
        RedisUtil.KeyOps.expire(token,30, TimeUnit.MINUTES);
//        YtoooToken token = new YtoooToken(sid);
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
//        getSubject(request, response).login(token);
        // 如果没有抛出异常则代表登入成功，返回true
        return true;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        //这里是个坑，如果不设置的接受的访问源，那么前端都会报跨域错误，因为这里还没到corsConfig里面
        httpServletResponse.setHeader("Access-Control-Allow-Origin", ((HttpServletRequest) request).getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json");
        httpServletResponse.getWriter().write(JSONObject.toJSON(RestResponse.fail(HttpStatus.UNAUTHORIZED.value(), "用户未登录")).toString());
        return false;
    }
}
