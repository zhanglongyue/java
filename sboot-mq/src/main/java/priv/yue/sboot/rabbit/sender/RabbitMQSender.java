package priv.yue.sboot.rabbit.sender;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import priv.yue.common.utils.JsonUtils;
import priv.yue.sboot.rabbit.callback.ConfirmCallback;
import priv.yue.sboot.rabbit.callback.ReturnsCallback;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @author ZhangLongYue
 * @since 2021/3/2 11:22
 */
@Component
public class RabbitMQSender {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private ConfirmCallback confirmCallback;

    @Resource
    private ReturnsCallback returnsCallback;

    private RabbitTemplate getRabbitTemplate() {
        rabbitTemplate.setConfirmCallback(confirmCallback);
        rabbitTemplate.setReturnCallback(returnsCallback);
        return rabbitTemplate;
    }

    public void sendJson(String exchange, String routingKey, final Object object, CorrelationData correlationData) {
        getRabbitTemplate().convertAndSend(exchange, routingKey, JsonUtils.toJson(object), correlationData);
    }

    public void sendJson(String exchange, String routingKey, final Object object) {
        getRabbitTemplate().convertAndSend(exchange, routingKey, JsonUtils.toJson(object), new CorrelationData(UUID.randomUUID().toString()));
    }

    public void sendObject(String exchange, String routingKey, final Object object, CorrelationData correlationData) {
        getRabbitTemplate().convertAndSend(exchange, routingKey, object, correlationData);
    }

    public void sendObject(String exchange, String routingKey, final Object object) {
        getRabbitTemplate().convertAndSend(exchange, routingKey, object, new CorrelationData(UUID.randomUUID().toString()));
    }

}
