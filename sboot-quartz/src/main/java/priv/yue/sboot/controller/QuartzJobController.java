package priv.yue.sboot.controller;

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
import priv.yue.sboot.base.BaseController;
import priv.yue.sboot.base.BaseDto;
import priv.yue.sboot.domain.QuartzJob;
import priv.yue.sboot.domain.maps.QuartzJobMap;
import priv.yue.sboot.dto.PageDto;
import priv.yue.sboot.rest.RestResponse;
import priv.yue.sboot.dto.QuartzJobDto;
import priv.yue.sboot.service.QuartzJobService;

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
    public RestResponse<Object> list(@Validated({QuartzJobDto.Query.class, Default.class}) QuartzJobDto quartzJobDto) {
        Map<String,Object> map = new HashMap<>();
        PageDto pager = quartzJobDto.getPager();
        map.put("search", quartzJobDto.getSearch());
        map.put("orderBy", getSortStr("quartz", pager.getSortBy(), pager.getSortDesc()));
        map.put("path", getUser().getDept().getPath());
        map.put("userDeptId", getUser().getDeptId());
        Page<QuartzJob> page = new Page<>(pager.getPage(), pager.getItemsPerPage());
        page = quartzJobService.selectPage(page, map);
        return RestResponse.success(page);
    }

    @ApiOperation("创建任务")
    @OpLog(title = "任务管理", businessType = FixedBusinessType.INSERT)
    @PostMapping("/create")
    @RequiresPermissions("quartz:add")
    public RestResponse<Object> create(@Validated({QuartzJobDto.Create.class}) QuartzJobDto quartzJobDto) {
        quartzJobDto.setDeptId(getUser().getDeptId());
        quartzJobService.create(quartzJobDto);
        return RestResponse.success();
    }

    @ApiOperation("删除任务")
    @OpLog(title = "任务管理", businessType = FixedBusinessType.DELETE)
    @PostMapping("/delete")
    @RequiresPermissions("quartz:del")
    public RestResponse<Object> delete(@RequestParam Long jobId){
        quartzJobService.delete(jobId);
        return RestResponse.success();
    }

    @ApiOperation("更新任务")
    @OpLog(title = "任务管理", businessType = FixedBusinessType.UPDATE)
    @PostMapping("/update")
    @RequiresPermissions("quartz:edit")
    public RestResponse<Object> update(@Validated({BaseDto.Update.class}) QuartzJobDto quartzJobDto){
        QuartzJob quartzJob = quartzJobMap.toEntity(quartzJobDto);
        quartzJobService.update(quartzJob);
        return RestResponse.success();
    }

    @ApiOperation("启用任务")
    @OpLog(title = "任务管理", businessType = FixedBusinessType.UPDATE)
    @PostMapping("/enable")
    @RequiresPermissions("quartz:edit")
    public RestResponse<Object> enable(@RequestParam Long jobId){
        quartzJobService.resume(jobId);
        return RestResponse.success();
    }

    @ApiOperation("停用任务")
    @OpLog(title = "任务管理", businessType = FixedBusinessType.UPDATE)
    @PostMapping("/disable")
    @RequiresPermissions("quartz:edit")
    public RestResponse<Object> disable(@RequestParam Long jobId){
        quartzJobService.pause(jobId);
        return RestResponse.success();
    }
}
