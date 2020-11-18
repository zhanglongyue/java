package com.longyue.springboot_shiro_ehcache.utils;

import cn.hutool.core.date.DateUtil;
import com.longyue.springboot_shiro_ehcache.common.Consts;
import com.longyue.springboot_shiro_ehcache.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@EnableConfigurationProperties(JwtConfig.class)
@Configuration
@Slf4j
@AllArgsConstructor
public class JwtUtil {

    private JwtConfig jwtConfig;
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 设置认证token
     *      id:用户id
     *      subject:用户名
     */
    public String createJwt(String id, String subject, Boolean rememberMe, Map<String,Object> map) {
        Date now = new Date();
        JwtBuilder jwtBuilder = Jwts.builder();
        //当设置的是整个map时，就需放在最前面，下面的setId等参数才会有效，否则会把前面set的值置空
        jwtBuilder.setClaims(map);
        jwtBuilder.setId(id);
        jwtBuilder.setSubject(subject);
        jwtBuilder.setIssuedAt(new Date());//设置当前时间
        jwtBuilder.signWith(SignatureAlgorithm.HS256, jwtConfig.getKey());//设置加密方式

        // 记住我时延长失效时间
        Long ttl = rememberMe ? jwtConfig.getRemember() : jwtConfig.getTtl();
        jwtBuilder.setExpiration(DateUtil.offsetMillisecond(now, ttl.intValue()));//设置过期时间

        String jwt = jwtBuilder.compact();
        stringRedisTemplate.opsForValue().set(Consts.REDIS_JWT_KEY_PREFIX + subject,
                                                jwt, ttl, TimeUnit.MILLISECONDS);
        return jwt;
    }

    /**
     * 解析token字符串获取clamis
     */
    public Claims parseJwt(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtConfig.getKey()).parseClaimsJws(token).getBody();
        return claims;
    }

}