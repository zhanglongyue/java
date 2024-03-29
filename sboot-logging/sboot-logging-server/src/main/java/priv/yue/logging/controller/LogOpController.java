package priv.yue.logging.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import priv.yue.common.base.BaseController;
import priv.yue.common.base.BaseDto;
import priv.yue.common.dto.PageDto;
import priv.yue.common.model.RestResult;
import priv.yue.common.model.RestResultUtils;
import priv.yue.logging.domain.LogOp;
import priv.yue.logging.dto.LogOpDto;
import priv.yue.logging.es7.domain.LogOpEs;
import priv.yue.logging.es7.service.LogOpEsService;
import priv.yue.logging.service.LogOpService;

import javax.validation.groups.Default;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ZhangLongYue
 * @since 2021/2/1 11:42
 */
@Api(tags = "操作日志")
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/logOp")
public class LogOpController extends BaseController {

    private LogOpService logOpService;
    private LogOpEsService logOpEsService;

    @ApiOperation("分页查询操作日志")
    @PostMapping("/list")
    @RequiresPermissions("logOp:query")
    public RestResult<Object> list(@Validated({BaseDto.Query.class, Default.class}) LogOpDto logOpDto) {
        PageDto pager = logOpDto.getPager();
        Map<String,Object> map = parameterMapBuild(logOpDto, pager);
        Page<LogOp> page = new Page<>(pager.getPage(), pager.getItemsPerPage());
        page = logOpService.selectPage(page, map);
        return RestResultUtils.success(page);
    }

    @ApiOperation("ES分页查询操作日志")
    @PostMapping("/listEs")
    @RequiresPermissions("logOp:query")
    public RestResult<Object> listEs(@Validated({BaseDto.Query.class, Default.class}) LogOpDto logOpDto) {
        PageDto pager = logOpDto.getPager();
        Map<String,Object> map = parameterMapBuild(logOpDto, pager);
        Page<LogOpEs> page = logOpEsService.selectPage(map, pager.getPage(), pager.getItemsPerPage());
        return RestResultUtils.success(page);
    }

    private Map<String, Object> parameterMapBuild(LogOpDto logOpDto, PageDto pager) {
        Map<String,Object> map = new HashMap<>();
        map.put("search", logOpDto.getSearch());
        map.put("businessType", logOpDto.getBusinessType());
        map.put("status", logOpDto.getStatus());
        map.put("createTimeBegin", logOpDto.getCreateTimeBegin());
        map.put("createTimeEnd", logOpDto.getCreateTimeEnd());
        map.put("orderBy", getSortStr("logOp", pager.getSortBy(), pager.getSortDesc()));
        map.put("path", getUser().getDept().getPath());
        map.put("userDeptId", getUser().getDeptId());
        return map;
    }
}
