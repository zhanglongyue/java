package priv.yue.sboot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "sboot")
public class AuthConfig {

    /**
     * 设置是否单一登录，一个用户只能有一个生效token
     */
    private Boolean singleLogin = false;

    /**
     * token默认超时时间30分
     */
    private Long defaultTokenTimeout = 1000L * 60 * 30;

    /**
     * 记住我默认超时时间7天
     */
    private Long remeberMeTimeout = 1000L * 60 * 60 * 24 * 7;

}
