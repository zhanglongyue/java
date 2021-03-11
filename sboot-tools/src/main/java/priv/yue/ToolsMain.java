package priv.yue;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * @author ZhangLongYue
 * @since 2021/3/8 13:56
 */

@EnableOpenApi
@SpringBootApplication
@MapperScan("priv.yue.sboot.mapper")
public class ToolsMain {

    public static void main(String[] args) {
        SpringApplication.run(ToolsMain.class, args);
    }

}
