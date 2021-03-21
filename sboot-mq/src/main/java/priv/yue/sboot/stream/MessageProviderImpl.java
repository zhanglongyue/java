package priv.yue.sboot.stream;


import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import priv.yue.common.utils.JsonUtils;

import javax.annotation.Resource;

/**
 * Stream消息源，包含发送方法
 * @author ZhangLongYue
 * @since 2020/12/29 19:39
 */
@Slf4j
@EnableBinding(Source.class)
public class MessageProviderImpl implements IMessageProvider {

    @Resource
    private MessageChannel output;  // 消息发送通道

    @Override
    public Boolean send(Object message) {
        Boolean success = output.send(MessageBuilder.withPayload(JsonUtils.toJson(message)).build());
        if (success) {
            log.info("向MQ发送消息: {}", message);
        } else {
            log.error("向MQ发送消息: {}", message);
        }
        return success;
    }
}
