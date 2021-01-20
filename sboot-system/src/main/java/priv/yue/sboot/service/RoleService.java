package priv.yue.sboot.service;


import priv.yue.sboot.domain.Role;

import java.util.List;

/**
 * 角色表服务接口
 *
 * @author zly
 * @since 2020-11-17 15:07:40
 * @description 由 Mybatisplus Code Generator 创建
 */
public interface RoleService extends BaseService<Role> {

    Role selectByPrimaryKey(Long roleId);

    List<Role> selectRolesByUser(Long userId);

    List<Role> selectRolesTreeByUser(Long userId);

}
