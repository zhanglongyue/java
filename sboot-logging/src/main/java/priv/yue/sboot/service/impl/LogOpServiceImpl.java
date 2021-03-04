package priv.yue.sboot.service.impl;

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
import priv.yue.sboot.base.BaseServiceImpl;
import priv.yue.sboot.config.RabbitMQLogConfig;
import priv.yue.sboot.domain.LogOp;
import priv.yue.sboot.log.LogOpCompletionHandler;
import priv.yue.sboot.mapper.LogOpMapper;
import priv.yue.sboot.service.LogOpService;
import priv.yue.sboot.utils.SpringBeanFactoryUtils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author ZhangLongYue
 * @since 2021/2/1 11:34
 */
@Slf4j
@AllArgsConstructor
@Service
@Transactional
public class LogOpServiceImpl extends BaseServiceImpl<LogOpMapper, LogOp> implements LogOpService {

    private LogOpMapper logOpMapper;

    @RabbitListener(
        bindings = @QueueBinding(
            value = @Queue(value = RabbitMQLogConfig.QUEUE, autoDelete = "false", durable = "true"),
            exchange = @Exchange(value = RabbitMQLogConfig.EXCHANGE, type = ExchangeTypes.TOPIC, durable = "true"),
            key = RabbitMQLogConfig.ROUTINGKEY
        )
    )
    public void complete(LogOp opLogInfo, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
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

    public Page<LogOp> selectPage(Page<LogOp> page, Map<String, Object> map) {
        return logOpMapper.selectPage(page, map);
    }
}
