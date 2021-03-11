package priv.yue.system.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import priv.yue.common.model.RestResult;

/**
 * @author ZhangLongYue
 * @since 2021/2/20 11:49
 */
@Component
@FeignClient("sboot-quartz")
public interface QuartzConsumerService {

    @PostMapping("/quartzJob/create")
    RestResult<Object> create();

}
