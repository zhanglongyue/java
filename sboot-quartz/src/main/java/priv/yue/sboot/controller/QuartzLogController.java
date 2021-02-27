package priv.yue.sboot.controller;

import cn.novelweb.tool.annotation.log.OpLog;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import priv.yue.sboot.domain.QuartzLog;
import priv.yue.sboot.dto.PageDto;
import priv.yue.sboot.dto.QuartzLogDto;
import priv.yue.sboot.rest.RestResponse;
import priv.yue.sboot.service.QuartzLogService;

import javax.validation.groups.Default;

/**
 * @author ZhangLongYue
 * @since 2021/2/26 10:17
 */
@Api(tags = "任务日志")
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/quartzLog")
public class QuartzLogController {

    private QuartzLogService quartzLogService;

    @ApiOperation("任务日志")
    @OpLog(title = "任务日志", businessType = "查询")
    @PostMapping("/list")
    @RequiresPermissions("quartz:query")
    public RestResponse<Object> list(@Validated({QuartzLogDto.Query.class, Default.class}) QuartzLogDto quartzLogDto) {
        PageDto pager = quartzLogDto.getPager();
        Page<QuartzLog> page = new Page<>(pager.getPage(), pager.getItemsPerPage());
        QueryWrapper<QuartzLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("job_id", quartzLogDto.getJobId());
        queryWrapper.orderByDesc("create_time");
        page = quartzLogService.page(page, queryWrapper);
        return RestResponse.success(page);
    }

}
