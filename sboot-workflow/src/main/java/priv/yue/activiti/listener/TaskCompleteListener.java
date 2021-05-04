package priv.yue.activiti.listener;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.impl.ActivitiEntityEventImpl;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.stereotype.Component;

/**
 * @author ZhangLongYue
 * @since 2021/5/4 11:00
 */
@Component
@Slf4j
public class TaskCompleteListener implements EventHandler {

    @Override
    public void handle(ActivitiEvent event) {
        ActivitiEntityEventImpl eventImpl = (ActivitiEntityEventImpl) event;
        TaskEntity taskEntity = (TaskEntity) eventImpl.getEntity();
        log.info(taskEntity + "complete");
    }
}
