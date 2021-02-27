package priv.yue.sboot.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import org.quartz.utils.ConnectionProvider;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author ZhangLongYue
 * @since 2021/2/19 16:32
 */
public class DruidConnectionProvider implements ConnectionProvider, ApplicationContextAware {

    private static ApplicationContext applicationContext;

    private DruidDataSource dataSource;

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void shutdown() {
        dataSource.close();
    }

    @Override
    public void initialize() {
        dataSource = (DruidDataSource) applicationContext.getBean(DataSource.class);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        DruidConnectionProvider.applicationContext = applicationContext;
    }
}

