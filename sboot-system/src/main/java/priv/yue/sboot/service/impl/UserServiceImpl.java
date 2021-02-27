package priv.yue.sboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import priv.yue.sboot.base.BaseServiceImpl;
import priv.yue.sboot.domain.Role;
import priv.yue.sboot.domain.User;
import priv.yue.sboot.domain.maps.UserMap;
import priv.yue.sboot.dto.UserDto;
import priv.yue.sboot.exception.BadRequestException;
import priv.yue.sboot.mapper.UserMapper;
import priv.yue.sboot.service.DeptService;
import priv.yue.sboot.service.RoleService;
import priv.yue.sboot.service.UserService;
import priv.yue.sboot.utils.SaltUtils;
import priv.yue.sboot.vo.LoginVo;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
public class UserServiceImpl extends BaseServiceImpl<UserMapper, User> implements UserService {

    private UserMapper userMapper;
    private DeptService deptService;
    private RoleService roleService;
    private UserMap userMap;

    public User selectByPK(Long userId){
        return userMapper.selectByPK(userId);
    }

    public User selectByName(String username) {
        return userMapper.selectByName(username);
    }

    public User save(UserDto userDto) {
        if (checkUsernameExist(userDto.getUsername())) {
            throw new BadRequestException("用户名[" + userDto.getUsername() + "]已存在" );
        }

        User user = checkInsertAndUpdate(userDto);
        LoginVo loginVo = (LoginVo) SecurityUtils.getSubject().getPrincipal();
        User currUser = loginVo.getUser();

        // 保存用户
        user.setSalt(SaltUtils.getSalt(8));
        user.setPassword(new Md5Hash(user.getPassword(), user.getSalt(), 1024).toHex());
        save(user);

        // 保存关联的角色信息
        for (Role role : new HashSet<>(user.getRoles())) {
            userMapper.insertUserRole(user.getUserId(), role.getRoleId());
        }
        return user;
    }

    public User update(UserDto userDto) {
        User user = getById(userDto.getUserId());
        if (!user.getUsername().equals(userDto.getUsername())
                && checkUsernameExist(userDto.getUsername())) {
            throw new BadRequestException("用户名[" + userDto.getUsername() + "]已存在" );
        }

        user = checkInsertAndUpdate(userDto);
        LoginVo loginVo = (LoginVo) SecurityUtils.getSubject().getPrincipal();
        User currUser = loginVo.getUser();

        // 更新用户
        updateById(user);

        // 先删除角色
        userMapper.deleteUserRole(user.getUserId());
        // 再保存角色信息
        for (Role role : user.getRoles()) {
            userMapper.insertUserRole(user.getUserId(), role.getRoleId());
        }
        return user;
    }

    public Page<User> selectPage(Page<User> page, Map<String, Object> map) {
        return userMapper.selectPage(page, map);
    }

    public List<User> selectPageOnce(Map<String, Object> map) {
        return userMapper.selectPageOnce(map);
    }

    public int selectPageCount(Map<String, Object> map){
        return userMapper.selectPageCount(map);
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
        User user = selectByName(username);
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