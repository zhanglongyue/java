package priv.yue.sboot.mapper;

import priv.yue.sboot.domain.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 角色表(sys_role)数据Mapper
 *
 * @author zly
 * @since 2020-11-17 15:07:40
 * @description 由 Mybatisplus Code Generator 创建
*/
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    List<Role> getUserRoles(Integer userId);
}
