package priv.yue;

import io.seata.spring.annotation.datasource.EnableAutoDataSourceProxy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.oas.annotations.EnableOpenApi;

@EnableOpenApi
@SpringBootApplication/*(exclude = {DataSourceAutoConfiguration.class})*/
@EnableDiscoveryClient
@EnableFeignClients
//@EnableAutoDataSourceProxy
public class SystemApp {

    public static void main(String[] args) {
        SpringApplication.run(SystemApp.class, args);
    }

}
