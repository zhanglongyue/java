package priv.yue.sboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import priv.yue.sboot.domain.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 系统菜单(sys_menu)数据Mapper
 *
 * @author zly
 * @since 2020-11-17 15:17:03
 * @description 由 Mybatisplus Code Generator 创建
*/
public interface MenuMapper extends BaseMapper<Menu> {
    List<Menu> getSubMenus(long pid);

    List<Menu> getMenusByRole(long roleId);

    List<Menu> getMenusByUser(long userId);

    List<Menu> getPermissionsByUser(long userId);
}