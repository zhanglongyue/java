package priv.yue.system.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import priv.yue.common.base.BaseService;
import priv.yue.common.domain.User;
import priv.yue.system.dto.UserDto;

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

    User selectByPK(Long userId);

    User selectByName(String username);

    User checkUser(String username, String password);

    User save(UserDto userDto);

    User update(UserDto userDto);

    Page<User> selectPage(Page<User> page, Map<String, Object> map);

    List<User> selectPageOnce(Map<String, Object> map);

    int selectPageCount(Map<String, Object> map);
}
