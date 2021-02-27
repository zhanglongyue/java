package priv.yue.sboot.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import priv.yue.sboot.base.BaseServiceImpl;
import priv.yue.sboot.domain.Dept;
import priv.yue.sboot.domain.Menu;
import priv.yue.sboot.domain.Role;
import priv.yue.sboot.domain.maps.RoleMap;
import priv.yue.sboot.mapper.RoleMapper;
import priv.yue.sboot.service.RoleService;
import priv.yue.sboot.dto.RoleDto;

import java.util.List;
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
    private RoleMap roleMap;

    public List<Role> buildTree(List<Role> roles){
        return buildTree(0L, roles, Role::getRoleId, Role::getPid, m -> m::setSubRole);
    }

    public Role selectByPK(Long roleId) {
        return roleMapper.selectByPK(roleId);
    }

    public List<Role> selectByUser(Long userId) {
        return roleMapper.selectByUser(userId);
    }

    /**
     * 获取用户所能看到的角色树
     * 根据用户拥有的角色生成角色树，如果树不完整，存在断点，
     * 则将处于断点的角色树与其他角色树同级展示
     */
    public List<Role> selectTreeByUser(Long userId) {
        List<Role> roles = roleMapper.selectTreeByUser(userId);
        return rebuildIncompleteTree(roles, Role::getRoleId, Role::getSubRole);
    }

    public List<Long> getChildrensIdsIncludeSelf(Long roleId) {
        List<Long> ids = getChildrensIds(roleId);
        ids.add(roleId);
        return ids;
    }

    public List<Long> getChildrensIds(Long roleId) {
        List<Role> roles = roleMapper.selectAllByPid(roleId);
        return flat(roles, Role::getSubRole).stream().map(Role::getRoleId).collect(Collectors.toList());
    }

    public List<Dept> getChildrens(Long roleId) {
        // TODO 暂时没用到
        return null;
    }

    public Role update(RoleDto roleDto) {
        Role role = roleMap.toEntity(roleDto);
        updateById(role);
        if (role.getMenus() != null && role.getMenus().size() > 0) {
            // 先删除角色的权限
            roleMapper.deleteRoleMenu(role.getRoleId());
            // 再保存角色权限信息
            for (Menu menu : role.getMenus()) {
                roleMapper.insertRoleMenu(menu.getMenuId(), role.getRoleId());
            }
        }
        return role;
    }

    public Role save(RoleDto roleDto) {
        Role role = roleMap.toEntity(roleDto);
        save(role);
        if (role.getMenus() != null && role.getMenus().size() > 0) {
            for (Menu menu : role.getMenus()) {
                roleMapper.insertRoleMenu(menu.getMenuId(), role.getRoleId());
            }
        }
        return role;
    }

    public int countUser(List<Long> ids) {
        return roleMapper.countUser(ids);
    }
}