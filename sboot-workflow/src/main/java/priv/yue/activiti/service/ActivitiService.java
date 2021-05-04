package priv.yue.activiti.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ZhangLongYue
 * @since 2021/5/3 15:34
 */
@Slf4j
@AllArgsConstructor
@Service
@Transactional
public class ActivitiService {

    private RuntimeService runtimeService;
    private RepositoryService repositoryService;
    private TaskService taskService;

    /**
     * 处理流程
     * @param procInstId 流程id
     * @param handleType 处理的方式
     */
    public void handle(String procInstId, String handleType) {
        Task task = getActiveTask(procInstId);
        taskService.setVariable(task.getId(), "type", handleType);
        taskService.complete(task.getId());
    }

    /**
     * 开始流程
     */
    public ProcessInstance startProcessInstanceByKey(String processDefinitionKey,
                                                     String businessKey, Map<String, Object> variables) {
        return runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);
    }

    /**
     * 获取流程中当前正在处理的任务
     */
    public Task getActiveTask(String procInstId) {
        return taskService.createTaskQuery().processInstanceId(procInstId).active().singleResult();
    }

    /**
     * 获取流程节点上的出口名称，可用于动态生成操作按钮
     */
    public List<String> getHandleType(String procInstId) {
        Task task = getActiveTask(procInstId);
        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        FlowElement flowElement = bpmnModel.getFlowElement(task.getTaskDefinitionKey());
        // 获取节点出口线段
        List<SequenceFlow> outgoingFlows = ((UserTask) flowElement).getOutgoingFlows();
        for (SequenceFlow outgoingFlow : outgoingFlows) {
            System.out.println(outgoingFlow);
        }
        return outgoingFlows.stream().map(FlowElement::getName).collect(Collectors.toList());
    }

    /**
     * 判断流程是否结束
     */
    public boolean isEnd(String procInstId) {
        ProcessInstanceQuery createProcessInstanceQuery = runtimeService.createProcessInstanceQuery();
        ProcessInstanceQuery processInstanceId = createProcessInstanceQuery.processInstanceId(procInstId);
        ProcessInstance singleResult = processInstanceId.singleResult();
        return singleResult == null;
    }

}
