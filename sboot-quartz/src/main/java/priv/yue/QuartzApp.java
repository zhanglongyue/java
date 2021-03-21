package priv.yue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * @author ZhangLongYue
 * @since 2021/2/19 16:21
 */
@EnableOpenApi
@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
public class QuartzApp {
    public static void main(String[] args) {
        SpringApplication.run(QuartzApp.class, args);
    }
}
