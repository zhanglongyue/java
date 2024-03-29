package priv.yue.quartz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import priv.yue.quartz.domain.QuartzJob;

import java.util.Map;

/**
 * 调度任务(quartz_job)数据Mapper
 *
 * @author ZhangLongYue
 * @since 2021/2/20 15:26
 */
@Mapper
public interface QuartzJobMapper extends BaseMapper<QuartzJob> {

//    QuartzJob selectByPK(Long id);
//
//    QuartzJob selectByName(String jobName);
//
    Page<QuartzJob> selectPage(Page<QuartzJob> page, @Param("map") Map<String, Object> map);
}
