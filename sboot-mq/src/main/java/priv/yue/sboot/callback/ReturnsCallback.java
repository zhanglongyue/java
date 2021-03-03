package priv.yue.sboot.callback;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * @author ZhangLongYue
 * @since 2021/3/2 10:31
 */
@Component
public class ReturnsCallback implements RabbitTemplate.ReturnCallback {

    /**
     * @param message 消息信息,因为message传递过来是字节，所以需要转换成字符串
     * @param replyCode 退回的状态码
     * @param replyText 退回的信息
     * @param exchange 交换机
     * @param routingKey 路由key
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        System.out.println("退回的消息是：" + new String(message.getBody()));
        System.out.println("退回的状态码是：" + replyCode);
        System.out.println("退回的信息是：" + replyText);
        System.out.println("退回的交换机是：" + exchange);
        System.out.println("退回的路由key是：" + routingKey);
    }

}
