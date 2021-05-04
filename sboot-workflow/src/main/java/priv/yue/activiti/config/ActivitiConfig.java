package priv.yue.activiti.config;

import lombok.AllArgsConstructor;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.ProcessEngineConfigurationConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import priv.yue.activiti.listener.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author ZhangLongYue
 * @since 2021/5/4 11:31
 */
@Component
@AllArgsConstructor
@Configuration
public class ActivitiConfig implements ProcessEngineConfigurationConfigurer {

    @Resource
    private TaskCreateListener taskCreateListener;
    @Resource
    private TaskCompleteListener taskCompleteListener;
    @Resource
    private ProcessCompleteListener processCompleteListener;

    @Override
    public void configure(SpringProcessEngineConfiguration processEngineConfiguration) {
        List<ActivitiEventListener> activitiEventListener = new ArrayList<>();
        activitiEventListener.add(globalEventListener());//配置全局监听器
        processEngineConfiguration.setEventListeners(activitiEventListener);
    }

    @Bean
    public GlobalEventListener globalEventListener(){
        GlobalEventListener globalEventListener = new GlobalEventListener();
        Map<ActivitiEventType, EventHandler> handlers = globalEventListener.getHandlers();
        handlers.put(ActivitiEventType.TASK_COMPLETED, taskCompleteListener);
        handlers.put(ActivitiEventType.TASK_CREATED, taskCreateListener);
        handlers.put(ActivitiEventType.PROCESS_COMPLETED, processCompleteListener);
        return globalEventListener;
    }


}
