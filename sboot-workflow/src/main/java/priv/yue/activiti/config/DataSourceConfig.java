package priv.yue.activiti.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.activiti.api.runtime.shared.identity.UserGroupManager;
import org.activiti.engine.cfg.ProcessEngineConfigurator;
import org.activiti.engine.impl.bpmn.parser.factory.DefaultActivityBehaviorFactory;
import org.activiti.engine.impl.persistence.StrongUuidGenerator;
import org.activiti.spring.SpringAsyncExecutor;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.AbstractProcessEngineAutoConfiguration;
import org.activiti.spring.boot.ActivitiProperties;
import org.activiti.spring.boot.ProcessDefinitionResourceFinder;
import org.activiti.spring.boot.ProcessEngineConfigurationConfigurer;
import org.activiti.spring.boot.process.validation.AsyncPropertyValidator;
import org.activiti.validation.ProcessValidatorImpl;
import org.activiti.validation.validator.ValidatorSet;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;

/**
 * @author ZhangLongYue
 * @since 2021/5/4 14:04
 */
@Configuration
public class DataSourceConfig extends AbstractProcessEngineAutoConfiguration {

    @Bean(name = "datasourceMain")
    @Primary
    @ConfigurationProperties("spring.datasource.main")
    public DruidDataSource dataSourceMain () {
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * 设置mybatis plus SqlSessionFactory数据源
     */
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory (@Qualifier("datasourceMain") DruidDataSource datasourceMain) throws Exception {
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        // 设置mybatis的数据源
        factoryBean.setDataSource(datasourceMain);

        // 全局配置
        GlobalConfig globalConfig  = new GlobalConfig();

        // 设置字段填充
        globalConfig.setMetaObjectHandler(new MetaObjectHandlerConfig());
        factoryBean.setGlobalConfig(globalConfig);

        // 设置分页
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        factoryBean.setPlugins(interceptor);

        return factoryBean.getObject();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.activiti.datasource")
    public DataSource activitiDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Primary
    public SpringProcessEngineConfiguration springProcessEngineConfiguration(
            PlatformTransactionManager transactionManager,
            SpringAsyncExecutor springAsyncExecutor,
            ActivitiProperties activitiProperties,
            ProcessDefinitionResourceFinder processDefinitionResourceFinder,
            @Autowired(required = false) ProcessEngineConfigurationConfigurer processEngineConfigurationConfigurer,
            @Autowired(required = false) List<ProcessEngineConfigurator> processEngineConfigurators,
            UserGroupManager userGroupManager) throws IOException {

        SpringProcessEngineConfiguration conf = new SpringProcessEngineConfiguration();
        conf.setConfigurators(processEngineConfigurators);
        configureProcessDefinitionResources(processDefinitionResourceFinder,
                conf);
        conf.setDataSource(activitiDataSource());
        conf.setTransactionManager(transactionManager);

        if (springAsyncExecutor != null) {
            conf.setAsyncExecutor(springAsyncExecutor);
        }
        conf.setDeploymentName(activitiProperties.getDeploymentName());
        conf.setDatabaseSchema(activitiProperties.getDatabaseSchema());
        conf.setDatabaseSchemaUpdate(activitiProperties.getDatabaseSchemaUpdate());
        conf.setDbHistoryUsed(activitiProperties.isDbHistoryUsed());
        conf.setAsyncExecutorActivate(activitiProperties.isAsyncExecutorActivate());
        if (!activitiProperties.isAsyncExecutorActivate()) {
            ValidatorSet springBootStarterValidatorSet = new ValidatorSet("activiti-spring-boot-starter");
            springBootStarterValidatorSet.addValidator(new AsyncPropertyValidator());
            if (conf.getProcessValidator() == null) {
                ProcessValidatorImpl processValidator = new ProcessValidatorImpl();
                processValidator.addValidatorSet(springBootStarterValidatorSet);
                conf.setProcessValidator(processValidator);
            } else {
                conf.getProcessValidator().getValidatorSets().add(springBootStarterValidatorSet);
            }
        }
        conf.setMailServerHost(activitiProperties.getMailServerHost());
        conf.setMailServerPort(activitiProperties.getMailServerPort());
        conf.setMailServerUsername(activitiProperties.getMailServerUserName());
        conf.setMailServerPassword(activitiProperties.getMailServerPassword());
        conf.setMailServerDefaultFrom(activitiProperties.getMailServerDefaultFrom());
        conf.setMailServerUseSSL(activitiProperties.isMailServerUseSsl());
        conf.setMailServerUseTLS(activitiProperties.isMailServerUseTls());

        if (userGroupManager != null) {
            conf.setUserGroupManager(userGroupManager);
        }

        conf.setHistoryLevel(activitiProperties.getHistoryLevel());
        conf.setCopyVariablesToLocalForTasks(activitiProperties.isCopyVariablesToLocalForTasks());
        conf.setSerializePOJOsInVariablesToJson(activitiProperties.isSerializePOJOsInVariablesToJson());
        conf.setJavaClassFieldForJackson(activitiProperties.getJavaClassFieldForJackson());

        if (activitiProperties.getCustomMybatisMappers() != null) {
            conf.setCustomMybatisMappers(
                    getCustomMybatisMapperClasses(activitiProperties.getCustomMybatisMappers()));
        }

        if (activitiProperties.getCustomMybatisXMLMappers() != null) {
            conf.setCustomMybatisXMLMappers(
                    new HashSet<>(activitiProperties.getCustomMybatisXMLMappers()));
        }

        if (activitiProperties.getCustomMybatisXMLMappers() != null) {
            conf.setCustomMybatisXMLMappers(
                    new HashSet<>(activitiProperties.getCustomMybatisXMLMappers()));
        }

        if (activitiProperties.isUseStrongUuids()) {
            conf.setIdGenerator(new StrongUuidGenerator());
        }

        if (activitiProperties.getDeploymentMode() != null) {
            conf.setDeploymentMode(activitiProperties.getDeploymentMode());
        }

        conf.setActivityBehaviorFactory(new DefaultActivityBehaviorFactory());

        if (processEngineConfigurationConfigurer != null) {
            processEngineConfigurationConfigurer.configure(conf);
        }

        return conf;
    }

    private void configureProcessDefinitionResources(
            ProcessDefinitionResourceFinder processDefinitionResourceFinder,
            SpringProcessEngineConfiguration conf) throws IOException {
        List<Resource> procDefResources = processDefinitionResourceFinder
                .discoverProcessDefinitionResources();
        if (!procDefResources.isEmpty()) {
            conf.setDeploymentResources(procDefResources.toArray(new Resource[0]));
        }
    }

    @Bean
    @ConditionalOnMissingBean
    @Primary
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }

}
