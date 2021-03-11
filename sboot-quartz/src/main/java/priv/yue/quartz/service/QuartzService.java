package priv.yue.quartz.service;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import priv.yue.quartz.domain.QuartzJob;

/**
 * @author ZhangLongYue
 * @since 2021/2/19 16:35
 */
@Service
public class QuartzService {

    @Autowired
    public QuartzService(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    private final Scheduler scheduler;

    @Value("${spring.quartz.defaultJobGroupName}")
    private String jobGroupName;

    @Value("${spring.quartz.defaultTriggerGroupName}")
    private String triggerGroupName;

    public void addJob(String jobName, Class<? extends Job> cls, String time) {
        addJob(jobName, cls, time, null);
    }

    public void addJob(String jobName, Class<? extends Job> cls, String time, Object parameter) {
        addJob(jobName, null, jobName, null, cls, time, parameter);
    }

    public void addJob(String jobName, String jobGroupName,
                       String triggerName, String triggerGroupName, Class<? extends Job> jobClass,
                       String time, Object parameter) {
        try {
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName).build();
            if (parameter != null){
                jobDetail.getJobDataMap().put(QuartzJob.JOB_KEY, parameter);
            }
            CronTrigger trigger = TriggerBuilder
                    .newTrigger()
                    .withIdentity(triggerName, triggerGroupName)
                    .withSchedule(CronScheduleBuilder.cronSchedule(time))
                    .build();
            scheduler.scheduleJob(jobDetail, trigger);
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateJobTime(String jobName, String time) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, triggerGroupName);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (trigger == null) {
                return;
            }
            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(time)) {
                JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
                JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                Class<? extends Job> objJobClass = jobDetail.getJobClass();
                removeJob(jobName);
                addJob(jobName, objJobClass, time);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /*public List<JobDetailFactoryBean> queryAllJob() throws SchedulerException {
        GroupMatcher<JobKey> anyJobGroup = GroupMatcher.anyJobGroup();
        Set<JobKey> jobKeys = scheduler.getJobKeys(anyJobGroup);
        List<JobDetailFactoryBean> result = getJobDetailsBeans(jobKeys);
        return result;
    }

    private List<JobDetailFactoryBean> getJobDetailsBeans(Collection<JobKey> jobKeys) throws SchedulerException {
        List<JobDetailFactoryBean> result = new ArrayList<>();
        for (JobKey jobKey : jobKeys) {
            List<? extends Trigger> triggersOfJob = scheduler.getTriggersOfJob(jobKey);
            for (Trigger trigger : triggersOfJob) {
                String group = jobKey.getGroup();
                String name = jobKey.getName();
                String className = jobKey.getClass().getName();
                String description = trigger.getDescription();
                JobDataMap jobDataMap = trigger.getJobDataMap();
                Date nextFireTime = trigger.getNextFireTime();
                Date previousFireTime = trigger.getPreviousFireTime();
                Trigger.TriggerState triggerState = scheduler.getTriggerState(TriggerKey.triggerKey(name, group));
                String cronExpression = "";
                if (trigger instanceof CronTrigger) {
                    cronExpression = ((CronTrigger) trigger).getCronExpression();
                }

                JobDetailFactoryBean build =
                        JobDetailFactoryBean.builder().className(className).groupName(group).jobName(name).description(description)
                        .cronExpression(cronExpression).jobDataMap(jobDataMap).nextFireTime(nextFireTime).previousFireTime(previousFireTime)
                        .triggerState(triggerState.name()).build();
                result.add(build);
            }
        }
        return result;
    }
*/

    public void runAJobNow(String jobName, String jobGroupName) throws SchedulerException {
        scheduler.triggerJob(new JobKey(jobName, jobGroupName));
    }

    public void runAJobNow(String jobName) throws SchedulerException {
        runAJobNow(jobName, null);
    }

    public void pauseJob(String jobName) throws SchedulerException {
        pauseJob(jobName, null);
    }

    public void pauseJob(String jobName, String jobGroupName) throws SchedulerException {
        scheduler.pauseJob(new JobKey(jobName, jobGroupName));
    }

    public void resumeJob(String jobName) throws SchedulerException {
        resumeJob(jobName, null);
    }

    public void resumeJob(String jobName, String jobGroupName) throws SchedulerException {
        scheduler.resumeJob(new JobKey(jobName, jobGroupName));
    }

    public void updateJobTime(String triggerName, String triggerGroupName, String time) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (trigger == null) {
                return;
            }
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(trigger.getCronExpression());
            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(time)) {
                trigger = trigger.getTriggerBuilder()
                        .withIdentity(triggerKey)
                        .withSchedule(scheduleBuilder)
                        .withSchedule(CronScheduleBuilder.cronSchedule(time))
                        .build();
                scheduler.rescheduleJob(triggerKey, trigger);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void removeJob(String jobName) {
        removeJob(jobName, null, jobName, null);
    }

    public void removeJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
            JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
            scheduler.pauseTrigger(triggerKey);
            scheduler.unscheduleJob(triggerKey);
            scheduler.deleteJob(jobKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void startJobs() {
        try {
            scheduler.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void shutdownJobs() {
        try {
            if (!scheduler.isShutdown()) {
                scheduler.shutdown();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
