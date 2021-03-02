package priv.yue.sboot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author ZhangLongYue
 * @since 2021/3/2 13:43
 */
@Configuration
public class RabbitMQConfig extends SimpleRabbitMQConfig {

    public static String defaultQueue;

    public static String defaultExchange;

    public static String routingKey;

    public static String getDefaultQueue() {
        return defaultQueue;
    }

    @Value("${mq.config.queue}")
    public void setDefaultQueue(String defaultQueue) {
        RabbitMQConfig.defaultQueue = defaultQueue;
    }

    public static String getDefaultExchange() {
        return defaultExchange;
    }

    @Value("${mq.config.exchange}")
    public void setDefaultExchange(String defaultExchange) {
        RabbitMQConfig.defaultExchange = defaultExchange;
    }

    public static String getRoutingKey() {
        return routingKey;
    }

    @Value("${mq.config.routingKey}")
    public void setRoutingKey(String routingKey) {
        RabbitMQConfig.routingKey = routingKey;
    }
}
