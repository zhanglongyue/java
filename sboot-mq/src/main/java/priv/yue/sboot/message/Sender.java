package priv.yue.sboot.message;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import priv.yue.sboot.callback.ConfirmCallback;
import priv.yue.sboot.callback.ReturnsCallback;
import priv.yue.sboot.utils.JsonUtils;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @author ZhangLongYue
 * @since 2021/3/2 11:22
 */
@Component
public class Sender {

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

    public void send(String exchange, String routingKey, final Object object, CorrelationData correlationData) {
        getRabbitTemplate().convertAndSend(exchange, routingKey, JsonUtils.toJson(object), correlationData);
    }

    public void send(String exchange, String routingKey, final Object object) {
        getRabbitTemplate().convertAndSend(exchange, routingKey, JsonUtils.toJson(object), new CorrelationData(UUID.randomUUID().toString()));
    }
}
