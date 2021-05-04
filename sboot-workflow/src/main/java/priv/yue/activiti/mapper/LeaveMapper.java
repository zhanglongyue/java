package priv.yue.activiti.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import priv.yue.activiti.domain.Leave;
import priv.yue.common.domain.User;

import java.util.Map;

/**
 * @author ZhangLongYue
 * @since 2021-05-02 15:13:41
 */

@Mapper
public interface LeaveMapper extends BaseMapper<Leave> {

    Page<Leave> selectPage(Page<Leave> page, @Param("map") Map<String, Object> map);

}




