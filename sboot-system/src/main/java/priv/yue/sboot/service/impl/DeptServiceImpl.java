package priv.yue.sboot.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import priv.yue.sboot.base.BaseServiceImpl;
import priv.yue.sboot.domain.Dept;
import priv.yue.sboot.mapper.DeptMapper;
import priv.yue.sboot.service.DeptService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 部门服务接口实现
 *
 * @author zly
 * @since 2020-11-17 15:13:41
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@AllArgsConstructor
@Service
@Transactional
public class DeptServiceImpl extends BaseServiceImpl<DeptMapper, Dept> implements DeptService {

    private DeptMapper deptMapper;

    public List<Dept> buildTree(List<Dept> depts){
        return buildTree(0L, depts, Dept::getDeptId, Dept::getPid, m -> m::setSubDept);
    }

    public List<Long> getChildrensIdsIncludeSelf(Long deptId) {
        List<Long> ids = getChildrensIds(deptId);
        ids.add(deptId);
        return ids;
    }

    public List<Long> getChildrensIds(Long deptId) {
        List<Dept> depts = deptMapper.selectAllByPid(deptId);
        return flat(depts, Dept::getSubDept).stream().map(Dept::getDeptId).collect(Collectors.toList());
    }

    /**
     * 只获取直系子类目
     */
    public List<Dept> getChildrens(Long deptId) {
        // TODO 暂时没用到
        return null;
    }

    public Dept selectByPK(Long deptId) {
        return deptMapper.selectByPK(deptId);
    }

    public List<Dept> selectAllByMap(Map<String, Object> map) {
        List<Dept> list = deptMapper.selectAllByMap(map);
        return buildTree(list);
    }

}