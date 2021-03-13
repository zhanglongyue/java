package priv.yue.gateway.config;

import org.springframework.boot.web.embedded.netty.NettyServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ZhangLongYue
 * @since 2021/3/13 14:40
 */
@Configuration
public class ServerConfig {

    /**
     * netty MaxHeaderSize 默认8k，这里设置大一些，不然用户信息过多会400
     * https://github.com/spring-cloud/spring-cloud-gateway/issues/481#issuecomment-411433919
     */
    @Bean
    public NettyServerCustomizer serverCustomizer() {
        return options -> options.httpRequestDecoder(spec -> spec.maxHeaderSize(100 * 1024));
    }
}
