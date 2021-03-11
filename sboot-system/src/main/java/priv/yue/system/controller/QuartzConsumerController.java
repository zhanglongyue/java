package priv.yue.system.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import priv.yue.common.model.RestResult;
import priv.yue.system.service.QuartzConsumerService;

/**
 * @author ZhangLongYue
 * @since 2021/2/20 10:39
 */
@Api(tags = "调度管理")
@Slf4j
@RestController
@RequestMapping("/quartz")
public class QuartzConsumerController {

    @Autowired
    private QuartzConsumerService quartzConsumerService;

    @PostMapping("/create")
    public RestResult<Object> create(){
        return quartzConsumerService.create();
    }

}
