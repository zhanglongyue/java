package priv.yue.sboot.service.impl;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import priv.yue.sboot.base.BaseServiceImpl;
import priv.yue.sboot.domain.Menu;
import priv.yue.sboot.domain.Role;
import priv.yue.sboot.domain.User;
import priv.yue.sboot.mapper.AuthMapper;
import priv.yue.sboot.service.AuthService;
import priv.yue.sboot.vo.LoginVo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 权限服务接口实现
 *
 * @author zly
 * @since 2020-11-16 15:35:00
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@AllArgsConstructor
@Service
@Transactional
public class AuthServiceImpl extends BaseServiceImpl<AuthMapper, Menu> implements AuthService {

    private AuthMapper authMapper;

    public LoginVo getLoginUserByName(String username) {
        LoginVo loginVo = new LoginVo();
        User user = authMapper.selectUserByName(username);
        if(user == null){
            throw new UnknownAccountException();
        }
        if(user.getEnabled() == 0){
            throw new AuthenticationException("账号被禁用");
        }
        List<Role> roles = authMapper.selectRoleByUser(user.getUserId());
        List<Menu> menus = authMapper.selectMenuByUser(user.getUserId());
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
        loginVo.setMenus(buildTree(0L, menus, Menu::getMenuId, Menu::getPid, m -> m::setSubMenu));
        return loginVo;
    }

}