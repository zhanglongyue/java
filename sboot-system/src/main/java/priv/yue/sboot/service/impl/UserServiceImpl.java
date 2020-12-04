package priv.yue.sboot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import priv.yue.sboot.mapper.UserMapper;
import priv.yue.sboot.domain.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import priv.yue.sboot.service.UserService;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统用户服务接口实现
 *
 * @author zly
 * @since 2020-11-16 15:35:00
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@AllArgsConstructor
@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private UserMapper userMapper;

    public User getUserById(Integer userId){
        return userMapper.getUserById(userId);
    }

    public User getUserByName(String username) {
        return userMapper.getUserByName(username);
    }

    public User checkUser(String username, String password) {
        User user = getUserByName(username);
        if(user == null){
            throw new UnknownAccountException();
        }
        String hashPassword = new Md5Hash(password, user.getSalt(), 1024).toHex();
        if(user.getPassword().equals(hashPassword)){
            return user;
        } else {
            throw new IncorrectCredentialsException();
        }
    }
}