package priv.yue.activiti.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import priv.yue.activiti.domain.Leave;
import priv.yue.activiti.domain.maps.LeaveMap;
import priv.yue.activiti.dto.LeaveDto;
import priv.yue.activiti.mapper.LeaveMapper;
import priv.yue.activiti.service.ActivitiService;
import priv.yue.activiti.service.LeaveActivitiService;
import priv.yue.activiti.service.LeaveService;
import priv.yue.common.base.BaseServiceImpl;
import priv.yue.common.exception.BadRequestException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ZhangLongYue
 * @since 2021-05-02 15:13:41
 */

@Slf4j
@AllArgsConstructor
@Service
@Transactional
public class LeaveActivitiServiceImpl extends LeaveServiceImpl implements LeaveActivitiService {

    private ActivitiService activitiService;

    /**
     * 创建请假单并开启流程
     */
    public Leave save(LeaveDto leaveDto) {
        // 保存请假单
        Leave leave = leaveMap.toEntity(leaveDto);
        leave.setUserId(getUser().getUserId())
                .setStatus("提交申请")
                .setDeptId(getUser().getDeptId())
                .setUsername(getUser().getUsername());
        // 计算天数
        leaveMapper.insert(leave);

        // 设置下一步处理人为创建者自己，需要自己先提交申请
        Map<String, Object> vars = new HashMap<>();
        vars.put("username", getUser().getUsername());

        // 启动流程
        ProcessInstance processInstance = activitiService.startProcessInstanceByKey(Leave.class.getSimpleName(),
                leave.getId().toString(), vars);

        // 将流程id保存到请假单中
        leave.setProcInstId(processInstance.getProcessInstanceId());
        updateById(leave);
        return leave;
    }

    @Override
    public Leave update(LeaveDto leaveDto) {
        Leave leave = leaveMap.toEntity(leaveDto);
        Task task = activitiService.getActiveTask(leave.getProcInstId());
        if (!task.getName().equals("提交申请")) {
            throw new BadRequestException("已申请状态无法修改");
        }
        leaveMapper.updateById(leave);
        return leave;
    }
}




