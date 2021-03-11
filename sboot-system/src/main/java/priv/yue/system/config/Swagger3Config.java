package priv.yue.system.config;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import priv.yue.auth.core.constant.Constants;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;
import java.util.List;

@Configuration
public class Swagger3Config {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
//                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(securityScheme())
                .securityContexts(securityContexts());
    }

    private List<SecurityScheme> securityScheme() {
        ApiKey apiKey = new ApiKey(Constants.AUTHORIZATION, Constants.AUTHORIZATION, "header");
        return Collections.singletonList(apiKey);
    }

    private List<SecurityContext> securityContexts() {
        SecurityContext  securityContext = SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.any())
                .build();
        return Collections.singletonList(securityContext);
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = new AuthorizationScope("global", "accessEverything");
        return Collections.singletonList(new SecurityReference(Constants.AUTHORIZATION, authorizationScopes));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Swagger3接口文档")
                .description("更多请咨询服务开发者Yue")
                .contact(new Contact("Yue", "https://", "zhanglongyue@vip.qq.com"))
                .version("1.0")
                .build();
    }
}
