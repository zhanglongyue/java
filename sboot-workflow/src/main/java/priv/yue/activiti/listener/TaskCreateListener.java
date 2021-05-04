package priv.yue.activiti.listener;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.impl.ActivitiEntityEventImpl;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.stereotype.Component;
import priv.yue.activiti.domain.Leave;
import priv.yue.activiti.service.LeaveService;

import javax.annotation.Resource;

/**
 * @author ZhangLongYue
 * @since 2021/5/4 11:00
 */
@Component
@Slf4j
public class TaskCreateListener implements EventHandler {

    @Resource
    private LeaveService leaveServiceImpl;

    @Override
    public void handle(ActivitiEvent event) {
        ActivitiEntityEventImpl eventImpl = (ActivitiEntityEventImpl) event;
        TaskEntity taskEntity = (TaskEntity) eventImpl.getEntity();
        log.info(taskEntity + "create");
        String leaveId = taskEntity.getProcessInstance().getBusinessKey();
        Leave leave = leaveServiceImpl.getById(leaveId);
        leave.setStatus(taskEntity.getName());
        leaveServiceImpl.updateById(leave);
    }

}
