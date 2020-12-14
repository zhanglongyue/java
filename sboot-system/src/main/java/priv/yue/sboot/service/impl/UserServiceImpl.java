package priv.yue.sboot.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import priv.yue.sboot.domain.Menu;
import priv.yue.sboot.domain.Role;
import priv.yue.sboot.domain.vo.LoginVo;
import priv.yue.sboot.mapper.MenuMapper;
import priv.yue.sboot.mapper.RoleMapper;
import priv.yue.sboot.mapper.UserMapper;
import priv.yue.sboot.domain.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import priv.yue.sboot.service.MenuService;
import priv.yue.sboot.service.UserService;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    private RoleMapper roleMapper;
    private MenuMapper menuMapper;
    private MenuService menuService;

    public User getUserById(long userId){
        return userMapper.getById(userId);
    }

    public User getUserByName(String username) {
        return userMapper.getByName(username);
    }

    public LoginVo getLoginUserByName(String username) {
        LoginVo loginVo = new LoginVo();
        User user = userMapper.getByNameNoRoles(username);
        if(user == null){
            throw new UnknownAccountException();
        }
        List<Role> roles = roleMapper.getRolesNoMenusByUser(user.getUserId());
        List<Menu> menus = menuMapper.getMenusByUserId(user.getUserId());
        Set<String> permissions = menus.stream()
                .filter(m -> StrUtil.isNotBlank(m.getPermission()))
                .map(Menu::getPermission)
                .collect(Collectors.toSet());
        loginVo.setUser(user);
        loginVo.setRoles(roles);
        loginVo.setPermissions(permissions);
        loginVo.setMenus(menuService.buildMenuTree(menus));
        return loginVo;
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

    public boolean deleteById(long id) {
        return userMapper.deleteById(id) == 1;
    }
}