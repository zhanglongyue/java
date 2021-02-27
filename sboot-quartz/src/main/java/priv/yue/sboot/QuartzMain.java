package priv.yue.sboot;

import org.mybatis.spring.annotation.MapperScan;
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
@MapperScan("priv.yue.sboot.mapper")
public class QuartzMain {
    public static void main(String[] args) {
        SpringApplication.run(QuartzMain.class, args);
    }
}
