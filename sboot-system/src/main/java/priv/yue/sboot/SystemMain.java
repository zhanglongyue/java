package priv.yue.sboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.oas.annotations.EnableOpenApi;

@EnableOpenApi
@SpringBootApplication
@EnableFeignClients
@MapperScan("priv.yue.sboot.mapper")
public class SystemMain {

    public static void main(String[] args) {
        SpringApplication.run(SystemMain.class, args);
    }

}
