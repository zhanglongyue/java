package priv.yue.sboot.stream;

/**
 * @author ZhangLongYue
 * @since 2020/12/29 19:38
 */
public interface IMessageProvider {

    /**
     * 消息发送，json格式字符串，消费方需要手动转换
     * 不使用Message<Object>来接收是因为如果消息转换出错，不会进入消费方的方法体，不方便手动捕捉处理
     * @param message 消息内容
     */
    Boolean send(Object message);

}
