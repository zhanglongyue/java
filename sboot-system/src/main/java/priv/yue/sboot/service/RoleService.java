package priv.yue.sboot.service;


import priv.yue.sboot.base.BaseService;
import priv.yue.sboot.domain.Dept;
import priv.yue.sboot.domain.Role;
import priv.yue.sboot.dto.RoleDto;

import java.util.List;

/**
 * 角色表服务接口
 *
 * @author zly
 * @since 2020-11-17 15:07:40
 * @description 由 Mybatisplus Code Generator 创建
 */
public interface RoleService extends BaseService<Role> {

    Role selectByPK(Long roleId);

    List<Role> selectByUser(Long userId);

    List<Role> selectTreeByUser(Long userId);

    List<Long> getChildrensIdsIncludeSelf(Long roleId);

    List<Long> getChildrensIds(Long roleId);

    List<Dept> getChildrens(Long roleId);

    int countUser(List<Long> ids);

    Role update(RoleDto roleDto);

    Role save(RoleDto roleDto);
}
