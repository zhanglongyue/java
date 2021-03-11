package priv.yue.quartz.controller;

import cn.novelweb.tool.annotation.log.OpLog;
import cn.novelweb.tool.annotation.log.pojo.FixedBusinessType;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import priv.yue.common.base.BaseController;
import priv.yue.common.base.BaseDto;
import priv.yue.quartz.domain.QuartzJob;
import priv.yue.quartz.domain.maps.QuartzJobMap;
import priv.yue.common.dto.PageDto;
import priv.yue.common.model.RestResult;
import priv.yue.quartz.dto.QuartzJobDto;
import priv.yue.common.model.RestResultUtils;
import priv.yue.quartz.service.QuartzJobService;

import javax.validation.groups.Default;
import java.util.HashMap;
import java.util.Map;


/**
 * @author ZhangLongYue
 * @since 2021/2/19 16:57
 */
@Api(tags = "任务调度")
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/quartzJob")
public class QuartzJobController extends BaseController {

    private QuartzJobService quartzJobService;

    private QuartzJobMap quartzJobMap;

    @ApiOperation("任务列表")
    @OpLog(title = "任务管理", businessType = "查询")
    @PostMapping("/list")
    @RequiresPermissions("quartz:query")
    public RestResult<Object> list(@Validated({QuartzJobDto.Query.class, Default.class}) QuartzJobDto quartzJobDto) {
        Map<String,Object> map = new HashMap<>();
        PageDto pager = quartzJobDto.getPager();
        map.put("search", quartzJobDto.getSearch());
        map.put("orderBy", getSortStr("quartz", pager.getSortBy(), pager.getSortDesc()));
        map.put("path", getUser().getDept().getPath());
        map.put("userDeptId", getUser().getDeptId());
        Page<QuartzJob> page = new Page<>(pager.getPage(), pager.getItemsPerPage());
        page = quartzJobService.selectPage(page, map);
        return RestResultUtils.success(page);
    }

    @ApiOperation("创建任务")
    @OpLog(title = "任务管理", businessType = FixedBusinessType.INSERT)
    @PostMapping("/create")
    @RequiresPermissions("quartz:add")
    public RestResult<Object> create(@Validated({QuartzJobDto.Create.class}) QuartzJobDto quartzJobDto) {
        quartzJobDto.setDeptId(getUser().getDeptId());
        quartzJobService.create(quartzJobDto);
        return RestResultUtils.success();
    }

    @ApiOperation("删除任务")
    @OpLog(title = "任务管理", businessType = FixedBusinessType.DELETE)
    @PostMapping("/delete")
    @RequiresPermissions("quartz:del")
    public RestResult<Object> delete(@RequestParam Long jobId){
        quartzJobService.delete(jobId);
        return RestResultUtils.success();
    }

    @ApiOperation("更新任务")
    @OpLog(title = "任务管理", businessType = FixedBusinessType.UPDATE)
    @PostMapping("/update")
    @RequiresPermissions("quartz:edit")
    public RestResult<Object> update(@Validated({BaseDto.Update.class}) QuartzJobDto quartzJobDto){
        QuartzJob quartzJob = quartzJobMap.toEntity(quartzJobDto);
        quartzJobService.update(quartzJob);
        return RestResultUtils.success();
    }

    @ApiOperation("启用任务")
    @OpLog(title = "启用任务", businessType = FixedBusinessType.UPDATE)
    @PostMapping("/enable")
    @RequiresPermissions("quartz:edit")
    public RestResult<Object> enable(@RequestParam Long jobId){
        quartzJobService.resume(jobId);
        return RestResultUtils.success();
    }

    @ApiOperation("停用任务")
    @OpLog(title = "停用任务", businessType = FixedBusinessType.UPDATE)
    @PostMapping("/disable")
    @RequiresPermissions("quartz:edit")
    public RestResult<Object> disable(@RequestParam Long jobId){
        quartzJobService.pause(jobId);
        return RestResultUtils.success();
    }

    @ApiOperation("立刻运行")
    @OpLog(title = "运行任务", businessType = "执行")
    @PostMapping("/run")
    @RequiresPermissions("quartz:edit")
    public RestResult<Object> run(@RequestParam Long jobId){
        quartzJobService.runNow(jobId);
        return RestResultUtils.success();
    }
}
