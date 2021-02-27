package priv.yue.sboot.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setMaxAge(3600L);             // 预检请求的有效期，单位为秒。
        corsConfiguration.setAllowCredentials(true);    // 是否支持安全证书(必需参数)
        return corsConfiguration;
    }

    @Bean
    public CorsFilter corsFilter(CorsConfiguration buildConfig) {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig);
        return new CorsFilter(source);
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> filterRegistrationBean(CorsFilter corsFilter){
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(corsFilter);
        // 设置优先级，不设置优先级将导致请求先被shiro的filter拦截，出现跨域问题
        bean.setOrder(0);
        return bean;
    }
}
