package priv.yue.sboot.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import priv.yue.sboot.rest.RestResponse;

/**
 * @author ZhangLongYue
 * @since 2021/2/20 11:49
 */
@Component
@FeignClient("sboot-quartz")
public interface QuartzConsumerService {

    @PostMapping("/quartzJob/create")
    RestResponse<Object> create();

}
