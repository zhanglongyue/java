package priv.yue.sboot.core;

import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import priv.yue.sboot.config.RabbitMQConfig;
import priv.yue.sboot.domain.QuartzJob;
import priv.yue.sboot.domain.QuartzLog;
import priv.yue.sboot.message.Sender;
import priv.yue.sboot.service.QuartzJobService;
import priv.yue.sboot.service.QuartzLogService;
import priv.yue.sboot.utils.JsonUtils;
import priv.yue.sboot.utils.ThrowableUtils;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 统一调度任务类，在此类中通过反射进行任务调度
 * @author ZhangLongYue
 * @since 2021/2/24 11:08
 */
@Slf4j
@Component
@DisallowConcurrentExecution
public class QuartzSerialJob extends QuartzJobBean {

    @Resource
    private QuartzJobService quartzJobService;

    @Resource
    private QuartzLogService quartzLogService;

    @Resource
    private Sender sender;

    @Value("${mq.config.quartzLogRoutingKey}")
    private String quartzLogRoutingKey;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        Long jobId = (Long) context.getMergedJobDataMap().get(QuartzJob.JOB_KEY);
        QuartzJob quartzJob = quartzJobService.getById(jobId);

        QuartzLog quartzLog = new QuartzLog();
        BeanUtils.copyProperties(quartzJob, quartzLog);
        quartzLog.setCreateTime(new Date());
        long startTime = System.currentTimeMillis();
        try {
            log.info("定时任务准备执行，任务名称：{}", quartzJob.getJobName());
            CallMethod callMethod = new CallMethod(quartzJob.getBeanName(), quartzJob.getMethodName(), quartzJob.getParams());
            callMethod.call();
            quartzLog.setIsSuccess(true);
            quartzJob.setLastExecIsSuccess(1);
            quartzJobService.updateById(quartzJob);
            long times = System.currentTimeMillis() - startTime;
            log.info("定时任务执行完毕，任务名称：{} 总共耗时：{} 毫秒", quartzLog.getJobName(), times);
        } catch (Exception e) {
            log.error("定时任务执行失败，任务名称：" + quartzJob.getJobName(), e);
            quartzLog.setIsSuccess(false);
            quartzLog.setExceptionDetail(ThrowableUtils.getStackTrace(e));
            quartzJob.setLastExecIsSuccess(0);
            // 任务如果失败了则暂停
            if(quartzJob.getPauseAfterFailure() != null && quartzJob.getPauseAfterFailure()){
                // 更新状态
                quartzJobService.pause(quartzJob);
            }
//            if(quartzJob.getEmail() != null){
//                EmailService emailService = SpringContextHolder.getBean(EmailService.class);
//                // 邮箱报警
//                if(StringUtils.isNoneBlank(quartzJob.getEmail())){
//                    EmailVo emailVo = taskAlarm(quartzJob, ThrowableUtil.getStackTrace(e));
//                    emailService.send(emailVo, emailService.find());
//                }
//            }
        } finally {
            long times = System.currentTimeMillis() - startTime;
            quartzLog.setTime(times);
            quartzLogService.save(quartzLog);
            // 执行失败后发送消息，消息将被WebSocketServer监听，并发送给前端，前端更新任务状态
            sender.send(RabbitMQConfig.defaultExchange, quartzLogRoutingKey, quartzJob);
        }
    }
}
