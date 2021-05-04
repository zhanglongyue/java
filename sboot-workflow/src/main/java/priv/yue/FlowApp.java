package priv.yue;

import cn.hutool.cron.TaskExecutor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * @author ZhangLongYue
 * @since 2021/5/1 15:04
 */
/*
  activiti6 不使用springsecurity 需要排除 (exclude = {SecurityAutoConfiguration.class})
  activiti7 不使用springsecurity需要排除以下类
(exclude = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration.class})
*/
@EnableOpenApi
@SpringBootApplication(
    exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
        org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration.class
    })
@EnableDiscoveryClient
public class FlowApp {

    public static void main(String[] args) {
        SpringApplication.run(FlowApp.class, args);
    }

}
