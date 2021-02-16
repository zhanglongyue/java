package priv.yue.sboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import priv.yue.sboot.domain.Dept;

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

    Dept selectByPK(Long deptId);

    List<Dept> selectAllByPid(Long pid);

    List<Dept> selectAllByMap(Map<String, Object> map);

}
