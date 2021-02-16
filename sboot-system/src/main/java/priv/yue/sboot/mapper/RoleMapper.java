package priv.yue.sboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import priv.yue.sboot.domain.Role;

import java.util.List;

/**
 * 角色表(sys_role)数据Mapper
 *
 * @author zly
 * @since 2020-11-17 15:07:40
 * @description 由 Mybatisplus Code Generator 创建
*/
public interface RoleMapper extends BaseMapper<Role> {

    Role selectByPK(Long roleId);

    List<Role> selectByUser(Long userId);

    List<Role> selectTreeByUser(Long userId);

    List<Role> selectAllByPid(Long pid);

    int countUser(List<Long> ids);

    int insertRoleMenu(Long menuId, Long roleId);

    int deleteRoleMenu(Long roleId);

}
