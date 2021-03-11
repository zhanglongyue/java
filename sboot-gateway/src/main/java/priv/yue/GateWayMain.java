package priv.yue;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * @author ZhangLongYue
 * @since 2021/3/9 14:04
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableOpenApi
@MapperScan("priv.yue.sboot.mapper")
public class GateWayMain {
    public static void main(String[] args) {
        SpringApplication.run(GateWayMain.class, args);
    }
}
