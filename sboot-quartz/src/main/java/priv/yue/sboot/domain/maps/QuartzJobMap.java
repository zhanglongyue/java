package priv.yue.sboot.domain.maps;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import priv.yue.sboot.base.BaseMap;
import priv.yue.sboot.domain.QuartzJob;
import priv.yue.sboot.dto.QuartzJobDto;

/**
 * QuartzJob转换类
 *
 * @author ZhangLongYue
 * @description
 * @since 2021/02/20 16:15
 */
@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface QuartzJobMap extends BaseMap<QuartzJobDto, QuartzJob> {
}