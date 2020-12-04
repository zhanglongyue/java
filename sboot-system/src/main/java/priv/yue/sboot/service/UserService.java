package priv.yue.sboot.service;


import com.baomidou.mybatisplus.extension.service.IService;
import priv.yue.sboot.domain.User;

/**
 * 系统用户服务接口
 *
 * @author zly
 * @since 2020-11-16 15:35:00
 * @description 由 Mybatisplus Code Generator 创建
 */
public interface UserService extends IService<User> {
    User getUserById(Integer userId);
    User getUserByName(String username);
    User checkUser(String username, String password);
}
