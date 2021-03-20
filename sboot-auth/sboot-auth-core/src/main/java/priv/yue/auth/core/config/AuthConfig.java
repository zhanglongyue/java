package priv.yue.auth.core.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import priv.yue.auth.core.constant.Constants;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "token")
public class AuthConfig {

    /**
     * 设置是否单一登录，一个用户只能有一个生效token
     */
    private Boolean singleLogin = false;

    /**
     * token默认超时时间30分
     */
    private Long defaultTokenTimeout = Constants.DEFAULT_TOKEN_TIMEOUT;

    /**
     * 记住我默认超时时间7天
     */
    private Long remeberMeTimeout = Constants.REMEBER_ME_TIMEOUT;

}
