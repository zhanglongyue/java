package priv.yue.logging.mq.listener;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import priv.yue.logging.domain.LogOp;
import priv.yue.logging.service.LogOpService;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * 日志消息订阅，接收MQ日志消息并保存日志
 * @author ZhangLongYue
 * @since 2021/3/19 9:11
 */
@Slf4j
@EnableBinding(Sink.class)
public class LogListener {

    @Resource
    private LogOpService logOpService;

    @StreamListener(Sink.INPUT)
    public void asyncLog(Message<LogOp> message, @Header(AmqpHeaders.CHANNEL) Channel channel,
                         @Header(AmqpHeaders.DELIVERY_TAG) long tag) {

        log.info("接收到日志消息: {}", message);

        LogOp logOp = message.getPayload();

        String str = "0:0:0:0:0:0:0:1";
        if (logOp.getIp().equals(str)) {
            logOp.setIp("127.0.0.1");
        }

        try {
            logOpService.save(logOp);
            // 手动ack
            // 参数1 deliveryTag（唯一标识 ID）：当一个消费者向 RabbitMQ 注册后，会建立起一个 Channel ，RabbitMQ 会用 basic.deliver 方法向消费者推送消息，这个方法携带了一个 delivery tag， 它代表了 RabbitMQ 向该 Channel 投递的这条消息的唯一标识 ID，是一个单调递增的正整数，delivery tag 的范围仅限于 Channel
            // 参数2 multiple：为了减少网络流量，手动确认可以被批处理，当该参数为 true 时，则可以一次性确认 delivery_tag 小于等于传入值的所有消息
            channel.basicAck(tag, false);
        } catch (Exception e) {
            log.error(ExceptionUtil.stacktraceToString(e, Integer.MAX_VALUE));
            try {
                //如果出现异常，则拒绝消息 可以重回队列 也可以丢弃 可以根据业务场景来
                //方式一：可以批量处理用：basicNack，传三个参数
                //参数3 标识是否重回队列 true 是重回  false 就是不重回:丢弃消息，如果重回队列的话，异常没有解决，就会进入死循环
                //channel.basicNack(tag, false, true);
                //方式二：不批量处理：basicReject，传两个参数，第二个参数是否批量
                channel.basicReject(tag,false);
            } catch (IOException ioException) {
                log.error(ExceptionUtil.stacktraceToString(ioException, Integer.MAX_VALUE));
            }
        }
    }

    /**
     * 消息接收者抛出异常时，错误信息将发送到input.group.errors通道，这里监听并处理
     */
    @ServiceActivator(inputChannel = "logExchange.logQueue.errors")
    public void error(Message<?> message) {
        log.error("[消息处理异常] {}", message);
    }

}
