package priv.yue.sboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import priv.yue.sboot.domain.Role;
import priv.yue.sboot.domain.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 系统用户(sys_user)数据Mapper
 *
 * @author zly
 * @since 2020-11-16 15:35:00
 * @description 由 Mybatisplus Code Generator 创建
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

    User getById(long id);

    User getByName(String username);

    User getByNameNoRoles(String username);

    int deleteById(long id);

    int setRoles(long userId, List<Role> roles);
}
