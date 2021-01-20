package priv.yue.sboot.service.impl;

import priv.yue.sboot.domain.Role;
import priv.yue.sboot.mapper.RoleMapper;
import priv.yue.sboot.service.RoleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 角色表服务接口实现
 *
 * @author zly
 * @since 2020-11-17 15:07:40
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@AllArgsConstructor
@Service
@Transactional
public class RoleServiceImpl extends BaseServiceImpl<RoleMapper, Role> implements RoleService {

    private RoleMapper roleMapper;

    public List<Role> buildTree(List<Role> roles){
        return buildTree(0L, roles, Role::getRoleId, Role::getPid, m -> m::setSubRole);
    }

    public Role selectByPrimaryKey(Long roleId) {
        return roleMapper.selectByPrimaryKey(roleId);
    }

    public List<Role> selectRolesByUser(Long userId) {
        return roleMapper.selectRolesByUser(userId);
    }

    /**
     * 获取用户所能看到的角色树
     * 根据用户拥有的角色生成角色树，如果树不完整，存在断点，
     * 则将处于断点的角色树与其他角色树同级展示
     */
    public List<Role> selectRolesTreeByUser(Long userId) {
        List<Role> roles = roleMapper.selectRolesTreeByUser(userId);
        return rebuildIncompleteTree(roles, Role::getRoleId, Role::getSubRole);
    }
}