package priv.yue.system.controller;

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
import priv.yue.common.domain.Job;
import priv.yue.common.domain.User;
import priv.yue.common.dto.PageDto;
import priv.yue.common.model.RestResult;
import priv.yue.common.model.RestResultUtils;
import priv.yue.system.domain.maps.JobMap;
import priv.yue.system.dto.JobDto;
import priv.yue.system.service.JobService;

import javax.validation.groups.Default;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统岗位服务控制器
 *
 * @author ZhangLongYue
 * @since 2020-11-17 15:17:03
 */
@Api(tags = "岗位管理")
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/job")
public class JobController extends BaseController {

    private JobService jobService;
    private JobMap jobMap;

    @ApiOperation("分页查询岗位列表")
    @OpLog(title = "岗位列表", businessType = "查询")
    @PostMapping("/list")
    @RequiresPermissions("job:query")
    public RestResult<Object> list(@Validated({JobDto.Query.class, Default.class}) JobDto jobDto) {
        Map<String,Object> map = new HashMap<>();
        PageDto pager = jobDto.getPager();
        map.put("search", jobDto.getSearch());
        map.put("orderBy", getSortStr("job", pager.getSortBy(), pager.getSortDesc()));
        map.put("path", getUser().getDept().getPath());
        map.put("userDeptId", getUser().getDeptId());
        Page<Job> page = new Page<>(pager.getPage(), pager.getItemsPerPage());
        page = jobService.selectPage(page, map);
        return RestResultUtils.success(page);
    }

    @ApiOperation("根据岗位id查询岗位信息")
    @OpLog(title = "岗位详情", businessType = "查询")
    @PostMapping("/get")
    @RequiresPermissions("job:query")
    public RestResult<Object> get(@RequestParam("jobId") Long jobId){
        return RestResultUtils.success(jobService.getById(jobId));
    }

    @ApiOperation("新增岗位")
    @OpLog(title = "岗位管理", businessType = FixedBusinessType.INSERT)
    @PostMapping("/create")
    @RequiresPermissions("job:add")
    public RestResult<Object> create(@Validated({JobDto.Create.class, Default.class}) JobDto jobDto){
        Job job = jobMap.toEntity(jobDto);
        jobService.save(job);
        return RestResultUtils.success();
    }

    @ApiOperation("更新岗位")
    @OpLog(title = "岗位管理", businessType = FixedBusinessType.UPDATE)
    @PostMapping("/update")
    @RequiresPermissions("job:edit")
    public RestResult<Object> update(@Validated({JobDto.Update.class, Default.class}) JobDto jobDto){
        Job job = jobMap.toEntity(jobDto);
        jobService.updateById(job);
        return RestResultUtils.success();
    }

    @ApiOperation("删除岗位")
    @OpLog(title = "岗位管理", businessType = FixedBusinessType.DELETE)
    @PostMapping("/delete")
    @RequiresPermissions("job:del")
    public RestResult<Object> delete(@RequestParam("jobId") Long jobId){
        jobService.removeById(jobId);
        return RestResultUtils.success();
    }

    @ApiOperation("禁用岗位")
    @OpLog(title = "禁用岗位", businessType = FixedBusinessType.UPDATE)
    @PostMapping("/disable")
    @RequiresPermissions("job:edit")
    public RestResult<Object> disable(@RequestParam("jobId") Long jobId){
        Job job = jobService.getById(jobId);
        jobService.updateById(job.setEnabled(0));
        return RestResultUtils.success();
    }

    @ApiOperation("启用岗位")
    @OpLog(title = "启用岗位", businessType = FixedBusinessType.UPDATE)
    @PostMapping("/enable")
    @RequiresPermissions("job:edit")
    public RestResult<Object> enable(@RequestParam("jobId") Long jobId){
        Job job = jobService.getById(jobId);
        jobService.updateById(job.setEnabled(1));
        return RestResultUtils.success();
    }

}