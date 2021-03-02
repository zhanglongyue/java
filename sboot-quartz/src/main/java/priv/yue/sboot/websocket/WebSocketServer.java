package priv.yue.sboot.websocket;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author ZhangLongYue
 * @since 2021/2/27 14:42
 */
@Slf4j
@ServerEndpoint("/websocket") //把当前类标识成一个WebSocket的服务端，值是访问的URL地址
@Component
public class WebSocketServer {

    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    // 存放session的集合
    private static CopyOnWriteArraySet<WebSocketServer> sessions = new CopyOnWriteArraySet<>();

    // 用于存所有的连接服务的客户端，这个对象存储是安全的（因为HashMap不支持线程同步）
    private static ConcurrentHashMap<String, WebSocketServer> webSocketSet = new ConcurrentHashMap<>();


    // 监听队列
    @RabbitListener(
        bindings = @QueueBinding(
            value = @Queue(value = "${mq.config.queue}", autoDelete = "false", durable = "true"),
            exchange = @Exchange(value = "${mq.config.exchange}", type = ExchangeTypes.TOPIC, durable = "true"),
            key = "quartz.log"
        )
    )
    public void getMessAge(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws Exception {

        System.out.println("发来消息：" + message);

        // 用来判断session中是否存在数据
        if (sessions.size() != 0) {
            for (WebSocketServer s : sessions) {
                if (s != null) {
                    s.session.getBasicRemote().sendText(message); // 向已连接客户端发送信息
                }
            }
        }

        // 手动ack
        channel.basicAck(tag, false);
    }


    //连接成功
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        sessions.add(this);
        log.info("[WebSocket] 连接成功，当前连接人数为：={}", webSocketSet.size());
    }

    //连接断开
    @OnClose
    public void onClose() {
        sessions.remove(this);
        log.info("[WebSocket] 退出成功，当前连接人数为：={}", webSocketSet.size());
    }

    //收到消息
    @OnMessage
    public String onMessage(String message) {
        log.info("[WebSocket] 收到消息：{}", message);
        return "你已成功连接！";
    }

}
