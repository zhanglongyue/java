package priv.yue.activiti.controller;

import cn.novelweb.tool.annotation.log.OpLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import priv.yue.activiti.service.ActivitiService;
import priv.yue.common.model.RestResult;
import priv.yue.common.model.RestResultUtils;

import java.util.HashMap;

/**
 * @author ZhangLongYue
 * @since 2021/5/5 17:38
 */
@Api(tags = "流程管理")
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/activiti")
public class ActivitiController {

    private ActivitiService activitiService;

    @ApiOperation("流程历史查询")
    @OpLog(title = "流程管理", businessType = "查询")
    @PostMapping("/history")
    @RequiresPermissions("leave:query")
    public RestResult<Object> history(@RequestParam("procInstId") String procInstId){
        return RestResultUtils.success(activitiService.historyActInstanceList(procInstId));
    }

    @ApiOperation("获取当前节点可处理方式")
    @OpLog(title = "流程管理", businessType = "查询")
    @PostMapping("/getHandleType")
    @RequiresPermissions("leave:handle")
    public RestResult<Object> getHandleType(@RequestParam("procInstId") String procInstId){
        return RestResultUtils.success(new HashMap<String, Object>() {{
            put("procInstId", procInstId);
            put("data", activitiService.getHandleType(procInstId));
        }});
    }
}
