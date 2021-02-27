package priv.yue.sboot.domain.maps;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import priv.yue.sboot.domain.Role;
import priv.yue.sboot.dto.RoleDto;

/**
 * Role转换类
 *
 * @author ZhangLongYue
 * @description
 * @since 2020/12/21 14:28
 */
@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface RoleMap extends BaseMap<RoleDto, Role> {
}
