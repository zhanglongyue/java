package priv.yue.activiti.listener;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ZhangLongYue
 * @since 2021/5/4 11:26
 */
public class GlobalEventListener implements ActivitiEventListener, Serializable {

    private static final long serialVersionUID = 54152525517584407L;

    private static Map<ActivitiEventType, EventHandler> handlers = new HashMap<>();

    @Override
    public void onEvent(ActivitiEvent event) {
        EventHandler eventHandler = handlers.get(event.getType());
        if(eventHandler!=null){
            eventHandler.handle(event);
        }
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }

    public Map<ActivitiEventType, EventHandler> getHandlers() {
        return handlers;
    }

    public void setHandlers(Map<ActivitiEventType, EventHandler> handlers) {
        this.handlers = handlers;
    }

}
