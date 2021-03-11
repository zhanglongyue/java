package priv.yue.logging.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import priv.yue.logging.constant.Constants;

/**
 * @author ZhangLongYue
 * @since 2021/3/2 13:43
 */
@Configuration
public class RabbitMQLogConfig {

    @Bean
    public Queue queue() {
        // durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
        // exclusive:默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
        // autoDelete:是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
        // return new Queue(DEFAULT_QUEUE,true,true,false);
        return QueueBuilder.durable(Constants.QUEUE).build();
    }

    /**
     * 使用主题交换机,支持‘#’和‘*’进行路由key匹配
     * ’#‘：零个或多个单词
     * ‘*’：必须存在一个单词
     * ‘.’：分割单词
     */
    @Bean
    public Exchange exchange() {
        return ExchangeBuilder.topicExchange(Constants.EXCHANGE).durable(true).build();
    }

    /**
     * 绑定交换机与队列
     * 这里routingKey使用了通配符 sboot.#
     */
    @Bean
    public Binding binding(Queue queue, Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(Constants.ROUTINGKEY).noargs();
    }
}
