package com.longyue.springboot_shiro_ehcache.auth.filter;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.longyue.springboot_shiro_ehcache.auth.token.DefaultToken;
import com.longyue.springboot_shiro_ehcache.common.Consts;
import com.longyue.springboot_shiro_ehcache.domain.User;
import com.longyue.springboot_shiro_ehcache.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationToken;

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
        User user = null;
        try {
            user = JSON.parseObject(RedisUtils.StringOps.get(Consts.TOKEN_PREFIX + token), User.class);
        } catch (Exception e) {
            return false;
        }
        if (ObjectUtil.isEmpty(user)) {
            return false;
        }
        //刷新超时时间
        RedisUtils.KeyOps.expire(token,30, TimeUnit.MINUTES);
        getSubject(request, response).login(new DefaultToken(token));
        return true;
    }

}
