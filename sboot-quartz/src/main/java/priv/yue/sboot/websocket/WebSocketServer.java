package priv.yue.sboot.websocket;

import com.rabbitmq.client.Channel;
import lombok.Data;
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
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author ZhangLongYue
 * @since 2021/2/27 14:42
 */
@Slf4j
@ServerEndpoint("/websocket/{userId}") //把当前类标识成一个WebSocket的服务端，值是访问的URL地址
@Component
@Data
public class WebSocketServer {

    // 用户userId
    private String userId;

    // 与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    // 用于存所有的连接服务的客户端，这个对象存储是安全的（因为HashMap不支持线程同步）
    private static ConcurrentHashMap<String, CopyOnWriteArraySet<WebSocketServer>> webSocketSet = new ConcurrentHashMap<>();

    // 监听队列
    @RabbitListener(
        bindings = @QueueBinding(
            value = @Queue(value = "${mq.config.queue}", autoDelete = "false", durable = "true"),
            exchange = @Exchange(value = "${mq.config.exchange}", type = ExchangeTypes.TOPIC, durable = "true"),
            key = "${mq.config.quartzLogRoutingKey}"
        )
    )
    public void getMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {

        try {
            for (Map.Entry<String, CopyOnWriteArraySet<WebSocketServer>> entry : webSocketSet.entrySet()) {
                CopyOnWriteArraySet<WebSocketServer> webSocketServers = entry.getValue();
                if (webSocketServers != null && webSocketServers.size() != 0) {
                    for (WebSocketServer webSocketServer : webSocketServers) {
                        webSocketServer.getSession().getBasicRemote().sendText(message);
                    }
                }
            }
            // 手动ack
            // 参数1 指定的是消息的序号（快递号）
            // 参数2 指定是否需要批量的签收 true为批量 false不批量
            channel.basicAck(tag, false);
        } catch (IOException e) {
            e.printStackTrace();
            try {
                //如果出现异常，则拒绝消息 可以重回队列 也可以丢弃 可以根据业务场景来
                //方式一：可以批量处理用：basicNack，传三个参数
                //参数3 标识是否重回队列 true 是重回  false 就是不重回:丢弃消息，如果重回队列的话，异常没有解决，就会进入死循环
                //channel.basicNack(tag, false, true);
                //方式二：不批量处理：basicReject，传两个参数，第二个参数是否批量
                channel.basicReject(tag,false);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }


    //连接成功
    @OnOpen
    public void onOpen(@PathParam(value = "userId") String userId, Session session) {
        this.session = session;
        this.userId = userId;
        CopyOnWriteArraySet<WebSocketServer> sessions = webSocketSet.get(userId);
        if (sessions == null) {
            sessions = new CopyOnWriteArraySet<>();
        }
        sessions.add(this);
        webSocketSet.put(userId, sessions); //加入map中
        log.info("[WebSocket] 连接成功，当前连接人数为：{}", webSocketSet.size());
    }

    //连接断开
    @OnClose
    public void onClose() {
        CopyOnWriteArraySet<WebSocketServer> sessions = webSocketSet.get(userId);
        sessions.remove(this);
        if (sessions.size() == 0) {
            webSocketSet.remove(userId);
        }
        log.info("[WebSocket] 退出成功，当前连接人数为：{}", webSocketSet.size());
    }

    //收到消息
    @OnMessage
    public String onMessage(String message) {
        log.info("[WebSocket] 收到消息：{}", message);
        return "websocket连接成功";
    }

}
