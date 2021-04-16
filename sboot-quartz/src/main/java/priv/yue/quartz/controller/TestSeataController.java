package priv.yue.quartz.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import priv.yue.common.base.BaseController;
import priv.yue.common.model.RestResult;
import priv.yue.common.model.RestResultUtils;
import priv.yue.quartz.domain.maps.QuartzJobMap;
import priv.yue.quartz.dto.QuartzJobDto;
import priv.yue.quartz.service.QuartzJobService;


/**
 * @author ZhangLongYue
 * @since 2021/2/19 16:57
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/seata")
public class TestSeataController extends BaseController {

    private QuartzJobService quartzJobService;

    private QuartzJobMap quartzJobMap;

    @PostMapping("/testSeata")
    public RestResult<Object> testSeata() {
        QuartzJobDto quartzJobDto = new QuartzJobDto();
        // 这里"0/5 * * * * /"故意写错模拟异常情况，看seata能否正常回滚
        quartzJobDto.setCronExpression("0/5 * * * * /");
        quartzJobDto.setEnabled(1);
        quartzJobDto.setJobName("test");
        quartzJobDto.setMethodName("runException");
        quartzJobDto.setBeanName("demoJob");
        quartzJobDto.setDeptId(getUser().getDeptId());
        quartzJobService.create(quartzJobDto);
        return RestResultUtils.success();
    }

}
