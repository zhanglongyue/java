package priv.yue.system.domain.maps;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import priv.yue.common.base.BaseMap;
import priv.yue.common.domain.User;
import priv.yue.system.dto.UserDto;

/**
 * User转换类
 *
 * @author ZhangLongYue
 * @description
 * @since 2020/12/21 14:28
 */
@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface UserMap extends BaseMap<UserDto, User> {
}
