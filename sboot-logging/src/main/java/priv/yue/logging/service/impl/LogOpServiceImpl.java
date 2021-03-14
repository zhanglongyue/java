package priv.yue.logging.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rabbitmq.client.Channel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import priv.yue.common.base.BaseServiceImpl;
import priv.yue.common.utils.JsonUtils;
import priv.yue.common.utils.SpringBeanFactoryUtils;
import priv.yue.logging.constant.Constants;
import priv.yue.logging.core.LogOpCompletionHandler;
import priv.yue.logging.domain.LogOp;
import priv.yue.logging.mapper.LogOpMapper;
import priv.yue.logging.service.LogOpService;

import java.io.IOException;
import java.util.Map;

/**
 * @author ZhangLongYue
 * @since 2021/2/1 11:34
 */
@Slf4j
@AllArgsConstructor
@Service
@Transactional
public class LogOpServiceImpl extends BaseServiceImpl<LogOpMapper, LogOp> implements LogOpService/*, LogOpCompletionHandler*/ {

    private LogOpMapper logOpMapper;

    @RabbitListener(
        bindings = @QueueBinding(
            value = @Queue(value = Constants.QUEUE, autoDelete = "false", durable = "true"),
            exchange = @Exchange(value = Constants.EXCHANGE, type = ExchangeTypes.TOPIC, durable = "true"),
            key = Constants.ROUTINGKEY
        )
    )
    public void complete(String json, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        /*System.out.println("操作日志结果:当前访问的ip地址:" + opLogInfo.getIp());
        System.out.println("操作日志结果:ip的实际地理位置:" + opLogInfo.getLocation());
        System.out.println("操作日志结果:浏览器的内核类型:" + opLogInfo.getBrowser());
        System.out.println("操作日志结果:浏览器的内核版本:" + opLogInfo.getBrowserVersion());
        System.out.println("操作日志结果:浏览器解析引擎类型:" + opLogInfo.getBrowserEngine());
        System.out.println("操作日志结果:浏览器解析引擎版本:" + opLogInfo.getBrowserEngineVersion());
        System.out.println("操作日志结果:是否为移动平台访问:" + opLogInfo.getIsMobile());
        System.out.println("操作日志结果:访问的操作系统类型:" + opLogInfo.getOs());
        System.out.println("操作日志结果:访问的操作平台类型:" + opLogInfo.getPlatform());
        System.out.println("操作日志结果:爬虫的类型(如果有):" + opLogInfo.getSpider());
        System.out.println("操作日志结果:除去host部分的路径:" + opLogInfo.getRequestUri());
        System.out.println("操作日志结果:获取出错时异常原因:" + opLogInfo.getErrorCause());
        System.out.println("操作日志结果:获取出错时异常信息:" + opLogInfo.getErrorMsg());
        System.out.println("操作日志结果:自定义访问模块名称:" + opLogInfo.getTitle());
        System.out.println("操作日志结果:访问的状态(0:正常):" + opLogInfo.getStatus());
        System.out.println("操作日志结果:获取到的访问的时间:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(opLogInfo.getCreateTime()));
        // ----------------------------------------- 下面的字段是 AccessLogInfo 没有的 -----------------------------------------
        System.out.println("操作日志结果:自定义访问业务类型:" + opLogInfo.getBusinessType());
        System.out.println("操作日志结果:执行操作的类的名称:" + opLogInfo.getClassName());
        System.out.println("操作日志结果:执行操作的方法名称:" + opLogInfo.getMethodName());
        System.out.println("操作日志结果:获取到访问的url参数:" + opLogInfo.getParameter());*/

        LogOp opLogInfo = JsonUtils.jsonToBean(json, LogOp.class);

        String str = "0:0:0:0:0:0:0:1";
        if (opLogInfo.getIp().equals(str)) {
            opLogInfo.setIp("127.0.0.1");
        }

        try {
            // 手动注入服务
            if (logOpMapper == null) {
                logOpMapper = SpringBeanFactoryUtils.getBean(LogOpMapper.class);
            }
            logOpMapper.insert(opLogInfo);
            // 手动ack
            // 参数1 deliveryTag（唯一标识 ID）：当一个消费者向 RabbitMQ 注册后，会建立起一个 Channel ，RabbitMQ 会用 basic.deliver 方法向消费者推送消息，这个方法携带了一个 delivery tag， 它代表了 RabbitMQ 向该 Channel 投递的这条消息的唯一标识 ID，是一个单调递增的正整数，delivery tag 的范围仅限于 Channel
            // 参数2 multiple：为了减少网络流量，手动确认可以被批处理，当该参数为 true 时，则可以一次性确认 delivery_tag 小于等于传入值的所有消息
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

    public Page<LogOp> selectPage(Page<LogOp> page, Map<String, Object> map) {
        return logOpMapper.selectPage(page, map);
    }
}
