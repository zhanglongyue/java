package priv.yue.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import priv.yue.common.domain.User;

import java.util.List;
import java.util.Map;

/**
 * 系统用户(sys_user)数据Mapper
 *
 * @author zly
 * @since 2020-11-16 15:35:00
 * @description
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

    int insertUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);

    int deleteUserRole(Long userId);

    User selectByPK(Long id);

    User selectByName(String username);

    User selectByNameNoRoles(String username);

    Page<User> selectPage(Page<User> page, @Param("map") Map<String, Object> map);

    /**
     * 该方法通过嵌套查询完成
     * 多表关联+主从表条件+分页+排序
     */
    List<User> selectPageOnce(@Param("map") Map<String, Object> map);

    /**
     * 使用selectPageOnce自定义分页查询时，使用自定义的count查询总数
     */
    int selectPageCount(@Param("map") Map<String, Object> map);

}
