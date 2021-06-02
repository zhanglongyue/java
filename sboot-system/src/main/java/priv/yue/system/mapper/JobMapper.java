package priv.yue.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import priv.yue.common.domain.Job;
import priv.yue.common.domain.User;

import java.util.List;
import java.util.Map;

/**
 * 部门(sys_job)数据Mapper
 *
 * @author zly
 * @since 2020-11-17 15:13:41
 * @description 由 Mybatisplus Code Generator 创建
*/
@Mapper
public interface JobMapper extends BaseMapper<Job> {

    Page<Job> selectPage(Page<Job> page, @Param("map") Map<String, Object> map);

}
