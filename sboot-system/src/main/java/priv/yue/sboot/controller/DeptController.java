package priv.yue.sboot.controller;

import cn.novelweb.tool.annotation.log.OpLog;
import cn.novelweb.tool.annotation.log.pojo.FixedBusinessType;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import priv.yue.sboot.rest.RestResponse;
import priv.yue.sboot.domain.Dept;
import priv.yue.sboot.domain.User;
import priv.yue.sboot.domain.maps.DeptMap;
import priv.yue.sboot.service.DeptService;
import priv.yue.sboot.service.UserService;
import priv.yue.sboot.dto.DeptDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 部门服务控制器
 *
 * @author ZhangLongYue
 * @since 2020-11-17 15:17:03
 * @description
 */
@Api(tags = "部门管理")
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/dept")
public class DeptController extends BaseController {

    private DeptService deptService;
    private DeptMap deptMap;
    private UserService userService;

    @ApiOperation("查询所有部门")
    @OpLog(title = "部门", businessType = "查询")
    @PostMapping("/list")
    @RequiresPermissions("dept:query")
    public RestResponse<Object> list(DeptDto deptDto){
        Map<String,Object> map = new HashMap<>();
        map.put("name", deptDto.getName());
        map.put("path", getUser().getDept().getPath());
        map.put("userDeptId", getUser().getDeptId());
        List<Dept> depts = deptService.selectAllByMap(map);
        return RestResponse.success(depts);
    }

    @ApiOperation("根据主键查询部门")
    @OpLog(title = "部门详情", businessType = "查询")
    @PostMapping("/get")
    @RequiresPermissions("dept:query")
    public RestResponse<Object> get(@RequestParam(name = "deptId") Long deptId){
        Dept dept = deptService.selectByPK(deptId);
        return RestResponse.success(dept);
    }

    @ApiOperation("新增部门")
    @OpLog(title = "部门管理", businessType = FixedBusinessType.INSERT)
    @PostMapping("/create")
    @RequiresPermissions("dept:add")
    public RestResponse<Object> create(@Validated({DeptDto.Create.class}) DeptDto deptDto){
        Dept dept = deptMap.toEntity(deptDto);
        Dept parentDept = deptService.selectByPK(dept.getPid());
        dept.setPath(parentDept.getPath() + dept.getPid() + ",");
        deptService.save(dept);
        return RestResponse.success();
    }

    @ApiOperation("更新部门")
    @OpLog(title = "部门管理", businessType = FixedBusinessType.UPDATE)
    @PostMapping("/update")
    @RequiresPermissions("dept:edit")
    public RestResponse<Object> update(@Validated({DeptDto.Update.class}) DeptDto deptDto){
        if (deptDto.getPid().equals(0L)) {
            return RestResponse.fail("顶级部门不允许更新");
        }
        Dept dept = deptMap.toEntity(deptDto);
        Dept parentDept = deptService.selectByPK(dept.getPid());
        dept.setPath(parentDept.getPath() + dept.getPid() + ",");
        deptService.updateById(dept);
        return RestResponse.success();
    }

    @ApiOperation("删除部门")
    @OpLog(title = "部门管理", businessType = FixedBusinessType.DELETE)
    @PostMapping("/delete")
    @RequiresPermissions("dept:del")
    public RestResponse<Object> delete(@RequestParam(name = "deptId") Long deptId){
        List<Long> ids = deptService.getChildrensIdsIncludeSelf(deptId);
        if (userService.count(new QueryWrapper<User>().in("dept_id", ids)) > 0) {
            return RestResponse.fail("无法删除存在用户的部门");
        }
        deptService.removeByIds(ids);
        return RestResponse.success();
    }

}