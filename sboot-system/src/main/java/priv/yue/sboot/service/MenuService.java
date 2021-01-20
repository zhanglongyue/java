package priv.yue.sboot.service;


import io.swagger.models.auth.In;
import priv.yue.sboot.domain.Menu;

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

    List<Menu> selectTreeByUser(Long userId);

    List<Menu> selectByUser(Long userId);

    Menu selectByPrimaryKey(Long menuId);

    List<Long> getMenuAndChildrensIds(Long menuId);

}
