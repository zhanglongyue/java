package priv.yue.sboot.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import priv.yue.sboot.domain.Dept;
import priv.yue.sboot.domain.Menu;
import priv.yue.sboot.domain.User;
import priv.yue.sboot.mapper.MenuMapper;
import priv.yue.sboot.service.MenuService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 系统菜单服务接口实现
 *
 * @author zly
 * @since 2020-11-17 15:17:03
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@AllArgsConstructor
@Service
@Transactional
public class MenuServiceImpl extends BaseServiceImpl<MenuMapper, Menu> implements MenuService {

    private MenuMapper menuMapper;

    public List<Menu> buildTree(List<Menu> menus){
        return buildTree(0L, menus, Menu::getMenuId, Menu::getPid, m -> m::setSubMenu);
    }

    public List<Long> getMenuAndChildrensIds(Long menuId) {
        List<Menu> menus = menuMapper.selectAllByPid(menuId);
        return Stream.concat(Stream.of(menuId),
                flat(menus, Menu::getSubMenu).stream().map(Menu::getMenuId)).collect(Collectors.toList());
    }

    public List<Menu> selectTreeByUser(Long userId){
        return buildTree(selectByUser(userId));
    }

    /**
     * 查询用户拥有的所有menu
     */
    public List<Menu> selectByUser(Long userId){
        List<Menu> menus = menuMapper.selectByUser(userId, null, null);
        return menus;
    }

    /**
     * 查询用户拥有的menu,可根据hidden筛选 1-隐藏 0-显示
     */
    public List<Menu> selectByUser(Long userId, Integer hidden){
        List<Menu> menus = menuMapper.selectByUser(userId, hidden, null);
        return menus;
    }

    /**
     * 查询用户拥有的menu,可根据hidden筛选 1-隐藏 0-显示,可根据类型筛选 in (type[])
     */
    public List<Menu> selectByUser(Long userId, Integer hidden, Integer[] type){
        List<Menu> menus = menuMapper.selectByUser(userId, hidden, type);
        return menus;
    }

    public Menu selectByPrimaryKey(Long menuId) {
        return menuMapper.selectByPrimaryKey(menuId);
    }

}