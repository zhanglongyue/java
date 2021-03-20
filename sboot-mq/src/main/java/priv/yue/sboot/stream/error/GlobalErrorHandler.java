package priv.yue.sboot.stream.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.context.IntegrationContextUtils;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * @author ZhangLongYue
 * @since 2021/3/20 14:08
 */
@Component
@Slf4j
public class GlobalErrorHandler {

    @StreamListener(IntegrationContextUtils.ERROR_CHANNEL_BEAN_NAME)
    public void error(Message<?> message) {
        log.error("[全局消息处理异常] {}", message);
    }

}
