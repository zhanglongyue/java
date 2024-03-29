package priv.yue.system.service;


import priv.yue.common.base.BaseService;
import priv.yue.common.domain.Dept;
import priv.yue.common.domain.Menu;

import java.util.List;

/**
 * 系统菜单服务接口
 *
 * @author zly
 * @since 2020-11-17 15:17:03
 * @description 由 Mybatisplus Code Generator 创建
 */
public interface MenuService extends BaseService<Menu> {

    List<Menu> buildTree(List<Menu> menus);

    List<Menu> selectByUser(Long userId, Integer hidden, Integer[] type);

    List<Menu> selectByUser(Long userId, Integer hidden);

    List<Menu> selectByUser(Long userId);

    List<Menu> selectTreeByUser(Long userId);

    Menu selectByPK(Long menuId);

    List<Long> getChildrensIdsIncludeSelf(Long menuId);

    List<Long> getChildrensIds(Long menuId);

    List<Dept> getChildrens(Long menuId);

}
