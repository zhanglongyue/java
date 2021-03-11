package priv.yue.system.service;


import priv.yue.common.base.BaseService;
import priv.yue.common.domain.Dept;

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

    List<Long> getChildrensIdsIncludeSelf(Long deptId);

    List<Long> getChildrensIds(Long deptId);

    List<Dept> getChildrens(Long deptId);

    Dept selectByPK(Long deptId);

    List<Dept> selectAllByMap(Map<String, Object> map);

}
