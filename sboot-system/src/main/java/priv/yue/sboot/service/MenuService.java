package priv.yue.sboot.service;


import com.baomidou.mybatisplus.extension.service.IService;
import priv.yue.sboot.domain.Menu;

import java.util.List;
import java.util.Map;

/**
 * 系统菜单服务接口
 *
 * @author zly
 * @since 2020-11-17 15:17:03
 * @description 由 Mybatisplus Code Generator 创建
 */
public interface MenuService extends IService<Menu> {

    List<Menu> buildMenuTree(List<Menu> menus);

    List<Menu> getMenusByUserId(long userId);

    List<Menu> queryMenusWithUser(Map<String, Object> queryMap);
}
