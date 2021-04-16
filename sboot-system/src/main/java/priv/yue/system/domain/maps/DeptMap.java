package priv.yue.system.domain.maps;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import priv.yue.common.base.BaseMap;
import priv.yue.common.domain.Dept;
import priv.yue.system.dto.DeptDto;

/**
 * Dept转换类
 * unmappedSourcePolicy = ReportingPolicy.IGNORE 忽略未映射的属性并且不获取任何输出警告
 *
 * @author ZhangLongYue
 * @description
 * @since 2020/12/21 14:28
 */
@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface DeptMap extends BaseMap<DeptDto, Dept> {
}