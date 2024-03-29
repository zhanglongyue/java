package priv.yue.quartz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import priv.yue.quartz.domain.QuartzLog;

/**
 * 调度任务(quartz_log)数据Mapper
 *
 * @author ZhangLongYue
 * @since 2021/2/20 15:26
 */
@Mapper
public interface QuartzLogMapper extends BaseMapper<QuartzLog> {

}
