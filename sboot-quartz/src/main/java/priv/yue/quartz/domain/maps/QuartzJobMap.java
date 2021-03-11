package priv.yue.quartz.domain.maps;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import priv.yue.common.base.BaseMap;
import priv.yue.quartz.domain.QuartzJob;
import priv.yue.quartz.dto.QuartzJobDto;

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