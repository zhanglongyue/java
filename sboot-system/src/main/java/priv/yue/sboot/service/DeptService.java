package priv.yue.sboot.service;


import priv.yue.sboot.domain.Dept;

import java.util.List;
import java.util.Map;

/**
 * 部门服务接口
 *
 * @author zly
 * @since 2020-11-17 15:13:41
 * @description 由 Mybatisplus Code Generator 创建
 */
public interface DeptService extends BaseService<Dept> {

    List<Long> getDeptAndChildrensIds(Long deptId);

    List<Dept> getDeptAndChildrens(Long deptId);

    Dept selectByPrimaryKey(Long deptId);

    List<Dept> selectAll(Map<String, Object> map);

}
