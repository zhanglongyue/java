package priv.yue.quartz.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import priv.yue.quartz.datasource.RoutingDataSource;
import priv.yue.quartz.datasource.DataSourceType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ZhangLongYue
 * @since 2021/2/22 15:10
 */
@Configuration
public class DataSourceConfig {

    @Bean(name = "datasourceMain")
    @Primary
    @ConfigurationProperties("spring.datasource.main")
    public DruidDataSource dataSourceMain () {
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * 指定quartz数据源
     */
    @Bean(name = "dataSourceQuartz")
    @ConfigurationProperties("spring.datasource.quartz")
    @QuartzDataSource
    public DruidDataSource dataSourceQuartz () { return DruidDataSourceBuilder.create().build(); }

    @Bean(name = "routingDataSource")
    public RoutingDataSource routingDataSource (@Qualifier("datasourceMain") DruidDataSource datasourceMain) {
        Map<Object, Object> map = new HashMap<>();
        map.put(DataSourceType.MAIN, datasourceMain);
        RoutingDataSource routingDataSource = new RoutingDataSource();
        routingDataSource.setTargetDataSources(map);
        routingDataSource.setDefaultTargetDataSource(datasourceMain);
        return routingDataSource;
    }

    /**
     * 设置mybatis plus SqlSessionFactory数据源
     */
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory (@Qualifier("datasourceMain") DruidDataSource datasourceMain) throws Exception {
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        // 设置mybatis的数据源
        factoryBean.setDataSource(routingDataSource(datasourceMain));

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
    public PlatformTransactionManager transactionManager (@Qualifier("routingDataSource") RoutingDataSource routingDataSource){
        return new DataSourceTransactionManager(routingDataSource);
    }
}

