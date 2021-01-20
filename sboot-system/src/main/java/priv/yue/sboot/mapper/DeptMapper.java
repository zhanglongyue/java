package priv.yue.sboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import priv.yue.sboot.domain.Dept;
import priv.yue.sboot.domain.Menu;

import java.util.List;
import java.util.Map;

/**
 * 部门(sys_dept)数据Mapper
 *
 * @author zly
 * @since 2020-11-17 15:13:41
 * @description 由 Mybatisplus Code Generator 创建
*/
public interface DeptMapper extends BaseMapper<Dept> {

    Dept selectByPrimaryKey(Long deptId);

    List<Dept> selectAllByPid(Long pid);

    List<Dept> selectAll(Map<String, Object> map);

}
