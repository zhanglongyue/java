package priv.yue.sboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import priv.yue.sboot.domain.User;

import java.util.List;
import java.util.Map;

/**
 * 系统用户(sys_user)数据Mapper
 *
 * @author zly
 * @since 2020-11-16 15:35:00
 * @description
*/
public interface UserMapper extends BaseMapper<User> {

    int setRole(Long userId, Long roleId);

    int deleteRole(Long userId);

    User selectByPrimaryKey(Long id);

    User selectByName(String username);

    User selectByNameNoRoles(String username);

    Page<User> selectByUser(Page<User> page, @Param("map") Map<String, Object> map);

    List<User> selectByUserOnce(@Param("map") Map<String, Object> map);

    Long selectByUserCount(@Param("map") Map<String, Object> map);

}
