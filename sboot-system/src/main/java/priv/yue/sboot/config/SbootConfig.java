package priv.yue.sboot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "sboot")
public class SbootConfig {

    /**
     * 设置是否单一登录，一个用户只能有一个生效token
     */
    private Boolean singleLogin = false;

}
