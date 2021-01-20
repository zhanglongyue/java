package priv.yue.sboot.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import priv.yue.sboot.domain.Menu;
import priv.yue.sboot.domain.Role;
import priv.yue.sboot.domain.User;
import priv.yue.sboot.domain.maps.UserMap;
import priv.yue.sboot.exception.BadRequestException;
import priv.yue.sboot.mapper.MenuMapper;
import priv.yue.sboot.mapper.RoleMapper;
import priv.yue.sboot.mapper.UserMapper;
import priv.yue.sboot.service.DeptService;
import priv.yue.sboot.service.MenuService;
import priv.yue.sboot.service.RoleService;
import priv.yue.sboot.service.UserService;
import priv.yue.sboot.service.dto.UserDto;
import priv.yue.sboot.utils.SaltUtils;
import priv.yue.sboot.vo.LoginVo;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.hutool.core.date.DateTime.now;

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
public class UserServiceImpl extends BaseServiceImpl<UserMapper, User> implements UserService {

    private UserMapper userMapper;
    private RoleMapper roleMapper;
    private MenuMapper menuMapper;
    private MenuService menuService;
    private DeptService deptService;
    private RoleService roleService;
    private UserMap userMap;

    public User getUserById(Long userId){
        return userMapper.selectByPrimaryKey(userId);
    }

    public User getUserByName(String username) {
        return userMapper.selectByName(username);
    }

    public LoginVo getLoginUserByName(String username) {
        LoginVo loginVo = new LoginVo();
        User user = userMapper.selectByNameNoRoles(username);
        if(user == null){
            throw new UnknownAccountException();
        }
        if(user.getEnabled() == 0){
            throw new AuthenticationException("账号被禁用");
        }
        List<Role> roles = roleMapper.selectRolesByUser(user.getUserId());
        List<Menu> menus = menuService.selectByUser(user.getUserId());
        Set<String> permissions = new HashSet<>();
        if (menus.size() > 0) {
            permissions = menus.stream()
                    .filter(m -> StrUtil.isNotBlank(m.getPermission()))
                    .map(Menu::getPermission)
                    .collect(Collectors.toSet());
        }
        // 过滤隐藏menu
        menus = menus.stream().filter(v -> v.getHidden() == 0).collect(Collectors.toList());
        loginVo.setUser(user);
        loginVo.setRoles(roles);
        loginVo.setPermissions(permissions);
        loginVo.setMenus(menuService.buildTree(menus));
        return loginVo;
    }

    public User insertUser(UserDto userDto) {
        if (checkUsernameExist(userDto.getUsername())) {
            throw new BadRequestException("用户名[" + userDto.getUsername() + "]已存在" );
        }

        User user = checkInsertAndUpdate(userDto);
        LoginVo loginVo = (LoginVo) SecurityUtils.getSubject().getPrincipal();
        User currUser = loginVo.getUser();

        // 保存用户
        user.setCreateBy(currUser.getUsername());
        user.setCreateTime(now());
        user.setEnabled(1);
        user.setSalt(SaltUtils.getSalt(8));
        user.setPassword(new Md5Hash(user.getPassword(), user.getSalt(), 1024).toHex());
        save(user);

        // 保存关联的角色信息
        for (Role role : new HashSet<>(user.getRoles())) {
            userMapper.setRole(user.getUserId(), role.getRoleId());
        }
        return user;
    }

    public User updateUser(UserDto userDto) {
        User user = getById(userDto.getUserId());
        if (!user.getUsername().equals(userDto.getUsername())
                && checkUsernameExist(userDto.getUsername())) {
            throw new BadRequestException("用户名[" + userDto.getUsername() + "]已存在" );
        }

        user = checkInsertAndUpdate(userDto);
        LoginVo loginVo = (LoginVo) SecurityUtils.getSubject().getPrincipal();
        User currUser = loginVo.getUser();

        // 更新用户
        user.setUpdateBy(currUser.getUsername())
            .setUpdateTime(now());
        updateById(user);

        // 先删除角色
        deleteRole(user.getUserId());
        // 再保存角色信息
        for (Role role : user.getRoles()) {
            userMapper.setRole(user.getUserId(), role.getRoleId());
        }
        return user;
    }

    public int deleteRole(Long userId) {
        return userMapper.deleteRole(userId);
    }

    public Page<User> selectByUser(Page<User> page, Map<String, Object> map) {
        return userMapper.selectByUser(page, map);
    }

    public List<User> selectByUserOnce(Map<String, Object> map) {
        return userMapper.selectByUserOnce(map);
    }

    public Long selectByUserCount(Map<String, Object> map){
        return userMapper.selectByUserCount(map);
    }

    public User checkInsertAndUpdate(UserDto userDto) {
        User user = userMap.toEntity(userDto);

        // 确认部门是否存在
        if (deptService.checkNotExistByColumn("dept_id", user.getDeptId())) {
            throw new BadRequestException("无效的部门");
        }

        // 确认角色是否存在
        Set<Long> rolesId = user.getRoles().stream().map(Role::getRoleId).collect(Collectors.toSet());
        List<Role> roles = roleService.list(new QueryWrapper<Role>().in("role_id", rolesId));
        if ( rolesId.size() != roles.size()) {
            throw new BadRequestException("无效的角色");
        }

        return user;
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

    public boolean checkUsernameExist(String username){
        return checkExistByColumn("username", username);
    }

    public boolean checkEmailExist(String email){
        return checkExistByColumn("email", email);
    }

}