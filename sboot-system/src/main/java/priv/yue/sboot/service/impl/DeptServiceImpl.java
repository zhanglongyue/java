package priv.yue.sboot.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import priv.yue.sboot.domain.Dept;
import priv.yue.sboot.domain.Menu;
import priv.yue.sboot.domain.User;
import priv.yue.sboot.domain.maps.DeptMap;
import priv.yue.sboot.mapper.DeptMapper;
import priv.yue.sboot.service.DeptService;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private DeptMap deptMap;

    public List<Dept> buildTree(List<Dept> depts){
        return buildTree(0L, depts, Dept::getDeptId, Dept::getPid, m -> m::setSubDept);
    }

    public List<Long> getDeptAndChildrensIds(Long deptId) {
        List<Dept> depts = deptMapper.selectAllByPid(deptId);
        return Stream.concat(Stream.of(deptId),
                flat(depts, Dept::getSubDept).stream().map(Dept::getDeptId)).collect(Collectors.toList());
    }

    @Override
    public List<Dept> getDeptAndChildrens(Long deptId) {
        return null;
    }

    @Override
    public Dept selectByPrimaryKey(Long deptId) {
        return deptMapper.selectByPrimaryKey(deptId);
    }

    @Override
    public List<Dept> selectAll(Map<String, Object> map) {
        List<Dept> list = deptMapper.selectAll(map);
        return buildTree(list);
    }

}