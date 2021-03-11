package priv.yue.quartz.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import priv.yue.quartz.domain.QuartzJob;
import priv.yue.quartz.dto.QuartzJobDto;

import java.util.Map;

/**
 * @author ZhangLongYue
 * @since 2021/2/20 15:43
 */
public interface QuartzJobService extends IService<QuartzJob> {

    QuartzJob create(QuartzJobDto quartzJobDto);

    Integer delete(Long jobId);

    Boolean update(QuartzJob quartzJob);

    Boolean pause(Long jobId);

    Boolean pause(QuartzJob quartzJob);

    Boolean resume(Long jobId);

    void runNow(Long jobId);

    Page<QuartzJob> selectPage(Page<QuartzJob> page, Map<String, Object> map);

}
