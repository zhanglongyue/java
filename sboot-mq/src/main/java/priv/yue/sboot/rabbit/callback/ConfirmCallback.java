package priv.yue.sboot.rabbit.callback;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * use
 * @author ZhangLongYue
 * @since 2021/3/2 10:27
 */
@Component
public class ConfirmCallback implements RabbitTemplate.ConfirmCallback {

    /**
     * @param correlationData 消息信息
     * @param ack  确认标识：true,MQ服务器exchange表示已经确认收到消息 false 表示没有收到消息
     * @param cause  如果没有收到消息，则指定为MQ服务器exchange消息没有收到的原因，如果已经收到则指定为null
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack){
            System.out.println("交换机收到消息，信息为：" + correlationData);
        } else {
            System.out.println("交换机未收到消息，原因为：" + cause);
        }
    }

}
