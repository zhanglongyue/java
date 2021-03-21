package priv.yue.logging.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import priv.yue.logging.domain.LogOp;

import java.util.Map;

/**
 * @author ZhangLongYue
 * @since 2021/2/1 11:30
 */
@Mapper
public interface LogOpMapper extends BaseMapper<LogOp> {

    int insert(LogOp logOp);

    Page<LogOp> selectPage(Page<LogOp> page, @Param("map") Map<String, Object> map);
}
