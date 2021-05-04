package priv.yue.activiti.controller;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import priv.yue.common.base.BaseController;

/**
 * @author ZhangLongYue
 * @since 2021/5/1 17:34
 */

@Api(tags = "请假审批")
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/leaveFlow")
public class LeaveFlowController extends BaseController {


}
