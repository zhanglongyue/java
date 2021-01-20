package priv.yue.sboot.config;

import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.Validator;

/**
 * validator配置
 *
 * @author ZhangLongYue
 * @description
 * @since 2020/12/21 16:44
 */
@Configuration
public class ValidatorConfig {

    @Bean
    public Validator validator(){
        ValidatorFactory validatorFactory = Validation.byProvider( HibernateValidator.class )
                .configure()
                // 配置快速失败策略 true开启 false关闭
                .addProperty( "hibernate.validator.fail_fast", "false" )
                .buildValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        return validator;
    }
}
