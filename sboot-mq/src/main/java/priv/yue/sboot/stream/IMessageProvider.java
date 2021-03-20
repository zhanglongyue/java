package priv.yue.sboot.stream;

/**
 * @author ZhangLongYue
 * @since 2020/12/29 19:38
 */
public interface IMessageProvider {

    /**
     * 消息发送
     * @param message 消息内容
     */
    Boolean send(Object message);

}
