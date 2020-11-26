package com.longyue.springboot_shiro_ehcache.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>
 * JWT 配置
 * </p>
 */
@Component
@ConfigurationProperties(prefix = "jwt.config")
@Data
public class JwtConfig {
    /**
     * jwt 加密 key，默认值：longyue.
     */
    private String key = "longyue";

    /**
     * jwt 过期时间，默认值：1800000 {@code 30 分钟}.
     */
    private Long ttl = 1800000L;

    /**
     * 开启 记住我 之后 jwt 过期时间，默认值 604800000 {@code 7 天}
     */
    private Long remember = 604800000L;
}
