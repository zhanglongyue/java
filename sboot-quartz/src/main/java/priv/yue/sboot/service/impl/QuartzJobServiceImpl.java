package priv.yue.sboot.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import priv.yue.sboot.base.BaseServiceImpl;
import priv.yue.sboot.core.QuartzSerialJob;
import priv.yue.sboot.domain.QuartzJob;
import priv.yue.sboot.domain.maps.QuartzJobMap;
import priv.yue.sboot.dto.QuartzJobDto;
import priv.yue.sboot.exception.BadRequestException;
import priv.yue.sboot.mapper.QuartzJobMapper;
import priv.yue.sboot.service.QuartzJobService;
import priv.yue.sboot.service.QuartzService;

import java.util.Map;

/**
 * @author ZhangLongYue
 * @since 2021/2/20 15:44
 */
@Slf4j
@AllArgsConstructor
@Service
@Transactional
public class QuartzJobServiceImpl extends BaseServiceImpl<QuartzJobMapper, QuartzJob> implements QuartzJobService {

    private QuartzJobMap quartzJobMap;

    private QuartzJobMapper quartzJobMapper;

    private QuartzService quartzService;

    public QuartzJob create(QuartzJobDto quartzJobDto){
        QuartzJob quartzJob = quartzJobMap.toEntity(quartzJobDto);
        quartzJobMapper.insert(quartzJob);

        // 新任务是启用时才添加调度任务
        if (quartzJobDto.getEnabled() == 1) {
            quartzService.addJob(quartzJob.getJobId().toString(), QuartzSerialJob.class,
                    quartzJob.getCronExpression(), quartzJob.getJobId());
        }
        return quartzJob;
    }

    public Integer delete(Long jobId){
        QuartzJob quartzJob = quartzJobMapper.selectById(jobId);
        quartzService.removeJob(quartzJob.getJobId().toString());
        return quartzJobMapper.deleteById(jobId);
    }

    @Override
    public Boolean update(QuartzJob quartzJob) {
        quartzService.removeJob(quartzJob.getJobId().toString());
        try {
            if (quartzJob.getEnabled() == 1) {
                quartzService.addJob(quartzJob.getJobId().toString(), QuartzSerialJob.class,
                        quartzJob.getCronExpression(), quartzJob.getJobId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestException("更新任务失败");
        }
        return updateById(quartzJob);
    }

    @Override
    public Boolean pause(Long jobId){
        QuartzJob quartzJob = getById(jobId);
        quartzJob.setEnabled(0);
        try {
            quartzService.pauseJob(jobId.toString());
        } catch (SchedulerException e) {
            throw new BadRequestException("停止任务失败" );
        }
        return updateById(quartzJob);
    }

    @Override
    public Boolean pause(QuartzJob quartzJob){
        quartzJob.setEnabled(0);
        try {
            quartzService.pauseJob(quartzJob.getJobId().toString());
        } catch (SchedulerException e) {
            e.printStackTrace();
            throw new BadRequestException("停止任务失败" );
        }
        return updateById(quartzJob);
    }

    @Override
    public Boolean resume(Long jobId){
        QuartzJob quartzJob = getById(jobId);
        quartzJob.setEnabled(1);
        try {
            return update(quartzJob);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadRequestException("启用任务失败" );
        }
    }

    @Override
    public Page<QuartzJob> selectPage(Page<QuartzJob> page, Map<String, Object> map) {
        return quartzJobMapper.selectPage(page, map);
    }
}
