package priv.yue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * @author ZhangLongYue
 * @since 2021/3/8 13:56
 */

@EnableOpenApi
@SpringBootApplication
public class ToolsApp {

    public static void main(String[] args) {
        SpringApplication.run(ToolsApp.class, args);
    }

}
