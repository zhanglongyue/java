package priv.yue.activiti.listener;

import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.impl.ActivitiEntityEventImpl;
import org.activiti.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.springframework.stereotype.Component;
import priv.yue.activiti.domain.Leave;
import priv.yue.activiti.service.LeaveService;

import javax.annotation.Resource;

/**
 * @author ZhangLongYue
 * @since 2021/5/4 15:22
 */
@Component
@Slf4j
public class ProcessCompleteListener implements EventHandler {

    @Resource
    private LeaveService leaveServiceImpl;

    @Override
    public void handle(ActivitiEvent event) {
        ActivitiEntityEventImpl eventImpl = (ActivitiEntityEventImpl) event;
        ExecutionEntityImpl executionEntity = (ExecutionEntityImpl) eventImpl.getEntity();
        log.info(executionEntity.getProcessInstance().getName() + "complete");
        String leaveId = executionEntity.getBusinessKey();
        Leave leave = leaveServiceImpl.getById(leaveId);
        leave.setStatus("结束");
        leaveServiceImpl.updateById(leave);
    }
}
