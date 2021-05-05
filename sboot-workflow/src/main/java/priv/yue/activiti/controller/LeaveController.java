package priv.yue.activiti.controller;

import cn.novelweb.tool.annotation.log.OpLog;
import cn.novelweb.tool.annotation.log.pojo.FixedBusinessType;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import priv.yue.activiti.domain.Leave;
import priv.yue.activiti.dto.LeaveDto;
import priv.yue.activiti.service.ActivitiService;
import priv.yue.activiti.service.LeaveActivitiService;
import priv.yue.activiti.service.LeaveService;
import priv.yue.common.base.BaseController;
import priv.yue.common.base.BaseDto;
import priv.yue.common.dto.PageDto;
import priv.yue.common.model.RestResult;
import priv.yue.common.model.RestResultUtils;

import javax.validation.groups.Default;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ZhangLongYue
 * @since 2021/5/1 17:34
 */

@Api(tags = "请假管理")
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/leave")
public class LeaveController extends BaseController {

    private TaskService taskService;
    private RuntimeService runtimeService;
    private LeaveService leaveServiceImpl;
    private RepositoryService repositoryService;
    private ActivitiService activitiService;
    private LeaveActivitiService leaveActivitiService;

    @ApiOperation("查询请假单")
    @OpLog(title = "请假单管理", businessType = "查询")
    @PostMapping("/list")
    @RequiresPermissions("leave:query")
    public RestResult<Object> list(@Validated({LeaveDto.Query.class, Default.class}) LeaveDto leaveDto){
        Map<String,Object> map = new HashMap<>();
        PageDto pager = leaveDto.getPager();
        map.put("search", leaveDto.getSearch());
        map.put("orderBy", getSortStr("leave", pager.getSortBy(), pager.getSortDesc()));
        map.put("path", getUser().getDept().getPath());
        map.put("userDeptId", getUser().getDeptId());
        Page<Leave> page = new Page<>(pager.getPage(), pager.getItemsPerPage());
        page = leaveServiceImpl.selectPage(page, map);
        return RestResultUtils.success(page);
    }

    @ApiOperation("根据id查询请假单")
    @OpLog(title = "请假单管理", businessType = "查询")
    @PostMapping("/get")
    @RequiresPermissions("leave:query")
    public RestResult<Object> get(@RequestParam("id") Long id){
        return RestResultUtils.success(leaveServiceImpl.getById(id));
    }

    @ApiOperation("删除请假单")
    @OpLog(title = "请假单管理", businessType = FixedBusinessType.DELETE)
    @PostMapping("/delete")
    @RequiresPermissions("leave:del")
    public RestResult<Object> delete(@RequestParam("id") Long id){
        leaveServiceImpl.removeById(id);
        return RestResultUtils.success();
    }

    @ApiOperation("创建请假单")
    @OpLog(title = "请假单管理", businessType = FixedBusinessType.INSERT)
    @PostMapping("/create")
    @RequiresPermissions("leave:add")
    public RestResult<Object> create(@Validated({BaseDto.Create.class, Default.class}) LeaveDto leaveDto){
        leaveActivitiService.save(leaveDto);
        return RestResultUtils.success();
    }

    @ApiOperation("修改请假单")
    @OpLog(title = "请假单管理", businessType = FixedBusinessType.UPDATE)
    @PostMapping("/update")
    @RequiresPermissions("leave:edit")
    public RestResult<Object> update(@Validated({LeaveDto.Update.class, Default.class}) LeaveDto leaveDto){
        leaveActivitiService.update(leaveDto);
        return RestResultUtils.success();
    }

    @ApiOperation("请假单处理")
    @OpLog(title = "请假单管理", businessType = "处理")
    @PostMapping("/handle")
    @RequiresPermissions("leave:handle")
    public RestResult<Object> handle(@RequestParam("procInstId") String procInstId,
                                     @RequestParam("handleType") String handleType){
        activitiService.handle(procInstId, handleType);
        return RestResultUtils.success();
    }

}
