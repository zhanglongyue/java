package priv.yue.sboot.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import priv.yue.sboot.domain.User;
import priv.yue.sboot.service.dto.UserDto;
import priv.yue.sboot.vo.LoginVo;

import java.util.List;
import java.util.Map;

/**
 * 系统用户服务接口
 *
 * @author zly
 * @since 2020-11-16 15:35:00
 * @description 由 Mybatisplus Code Generator 创建
 */
public interface UserService extends BaseService<User> {

    User getUserById(Long userId);

    User getUserByName(String username);

    User checkUser(String username, String password);

    LoginVo getLoginUserByName(String username);

    User insertUser(UserDto userDto);

    User updateUser(UserDto userDto);

    int deleteRole(Long userId);

    Page<User> selectByUser(Page<User> page, Map<String, Object> map);

    List<User> selectByUserOnce(Map<String, Object> map);

    Long selectByUserCount(Map<String, Object> map);
}
