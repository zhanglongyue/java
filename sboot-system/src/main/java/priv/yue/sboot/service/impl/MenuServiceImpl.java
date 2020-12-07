package priv.yue.sboot.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import priv.yue.sboot.domain.Menu;
import priv.yue.sboot.mapper.MenuMapper;
import priv.yue.sboot.service.MenuService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    private MenuMapper menuMapper;

    public List<Menu> buildMenuTree(List<Menu> menus){
        if (ObjectUtil.isEmpty(menus)) {
            return Collections.emptyList();
        }
        Map<Long, Menu> idMap = menus.parallelStream().collect(Collectors.toMap(Menu::getMenuId, m -> m));
        Map<Long, List<Menu>> pidGroupMenusMap = menus.parallelStream().collect(Collectors.groupingBy(Menu::getPid));
        pidGroupMenusMap.forEach((k, v) -> {
            Menu parentMenu = idMap.get(k);
            if (parentMenu != null) {
                parentMenu.setSubMenu(v);
            }
        });
        return pidGroupMenusMap.get(0);
    }

    public  List<Menu> getMenusByUser(long userId){
        return menuMapper.getMenusByUser(userId);
    }

}