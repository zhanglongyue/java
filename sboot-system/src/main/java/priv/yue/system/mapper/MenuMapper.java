package priv.yue.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import priv.yue.common.domain.Menu;

import java.util.List;

/**
 * 系统菜单(sys_menu)数据Mapper
 *
 * @author zly
 * @since 2020-11-17 15:17:03
 * @description 由 Mybatisplus Code Generator 创建
*/
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    Menu selectByPK(Long menuId);

    List<Menu> selectByRole(Long roleId);

    List<Menu> selectByUser(@Param("userId") Long userId,
                            @Param("hidden") Integer hidden,
                            @Param("type") Integer[] type);

    List<Menu> selectPermissionsByUser(Long userId);

    List<Menu> selectAll();

    List<Menu> selectAllByPid(Long pid);
}
