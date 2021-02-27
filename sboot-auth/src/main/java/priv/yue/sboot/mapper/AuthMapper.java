package priv.yue.sboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import priv.yue.sboot.domain.Menu;
import priv.yue.sboot.domain.Role;
import priv.yue.sboot.domain.User;

import java.util.List;

/**
 * @author ZhangLongYue
 * @since 2021/2/21 13:23
 */
public interface AuthMapper extends BaseMapper<Menu> {

    User selectUserByName(String username);

    List<Role> selectRoleByUser(Long userId);

    List<Menu> selectMenuByUser(Long userId);

}
