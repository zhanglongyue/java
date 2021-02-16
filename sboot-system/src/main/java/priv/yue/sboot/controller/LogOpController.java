package priv.yue.sboot.controller;

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
import priv.yue.sboot.common.RestResponse;
import priv.yue.sboot.domain.LogOp;
import priv.yue.sboot.service.LogOpService;
import priv.yue.sboot.service.dto.BaseDto;
import priv.yue.sboot.service.dto.LogOpDto;
import priv.yue.sboot.service.dto.PageDto;

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
public class LogOpController extends BaseController{

    private LogOpService logOpService;

    @ApiOperation("分页查询操作日志")
    @PostMapping("/list")
    @RequiresPermissions("logOp:query")
    public RestResponse<Object> list(@Validated({BaseDto.Query.class, Default.class}) LogOpDto logOpDto) {
        Map<String,Object> map = new HashMap<>();
        PageDto pager = logOpDto.getPager();
        map.put("search", logOpDto.getSearch());
        map.put("businessType", logOpDto.getBusinessType());
        map.put("status", logOpDto.getStatus());
        map.put("createTimeBegin", logOpDto.getCreateTimeBegin());
        map.put("createTimeEnd", logOpDto.getCreateTimeEnd());
        map.put("orderBy", getSortStr("logOp", pager.getSortBy(), pager.getSortDesc()));
        map.put("path", getUser().getDept().getPath());
        map.put("userDeptId", getUser().getDeptId());
        Page<LogOp> page = new Page<>(pager.getPage(), pager.getItemsPerPage());
        page = logOpService.selectPage(page, map);
        return RestResponse.success(page);
    }
}
