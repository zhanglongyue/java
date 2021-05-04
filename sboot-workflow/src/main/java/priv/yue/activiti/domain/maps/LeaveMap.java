package priv.yue.activiti.domain.maps;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import priv.yue.activiti.domain.Leave;
import priv.yue.activiti.dto.LeaveDto;
import priv.yue.common.base.BaseMap;

/**
 * Leave转换类
 *
 * @author ZhangLongYue
 * @description
 * @since 2020/12/21 14:28
 */
@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface LeaveMap extends BaseMap<LeaveDto, Leave> {
}
