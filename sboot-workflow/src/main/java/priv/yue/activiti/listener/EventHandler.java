package priv.yue.activiti.listener;

import org.activiti.engine.delegate.event.ActivitiEvent;

/**
 * 定义自己的事件处理器接口，所有自定义的事件处理器必须实现这个接口
 *
 * @author ZhangLongYue
 * @since 2021/5/4 11:19
 */
public interface EventHandler {
    void handle(ActivitiEvent event);
}
