package priv.yue;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * @author ZhangLongYue
 * @since 2021/3/9 20:01
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableOpenApi
@MapperScan({"priv.yue.auth.mapper","priv.yue.logging.mapper"})
public class AuthMain {
    public static void main(String[] args) {
        SpringApplication.run(AuthMain.class, args);
    }
}
