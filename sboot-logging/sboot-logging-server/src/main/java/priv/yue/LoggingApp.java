package priv.yue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import springfox.documentation.oas.annotations.EnableOpenApi;


/**
 * @author ZhangLongYue
 * @since 2021/3/11 10:15
 */
@EnableOpenApi
@SpringBootApplication
@EnableDiscoveryClient
public class LoggingApp {
    public static void main(String[] args) {
        SpringApplication.run(LoggingApp.class, args);
    }
}
