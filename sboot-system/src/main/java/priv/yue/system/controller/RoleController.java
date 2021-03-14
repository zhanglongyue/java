package priv.yue.system.controller;


import cn.novelweb.tool.annotation.log.OpLog;
import cn.novelweb.tool.annotation.log.pojo.FixedBusinessType;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
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
import priv.yue.common.domain.Role;
import priv.yue.common.model.RestResult;
import priv.yue.system.domain.maps.RoleMap;
import priv.yue.common.model.RestResultUtils;
import priv.yue.system.dto.UserDto;
import priv.yue.system.service.RoleService;
import priv.yue.system.dto.RoleDto;

import java.util.List;

/**
 * 角色表服务控制器
 *
 * @author ZhangLongYue
 * @since 2020-11-17 15:17:03
 * @description
 */
@Api(tags = "角色管理")
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/role")
public class RoleController extends BaseController {
    private final RoleService roleService;
    private RoleMap roleMap;

    @ApiOperation(value = "查询角色树", notes = "只能查询当前用户角色及子角色")
    @OpLog(title = "角色树", businessType = "查询")
    @PostMapping("/list")
    @RequiresPermissions("role:query")
    public RestResult<Object> list(){
        List<Role> roleList = roleService.selectTreeByUser(getUser().getUserId());
        return RestResultUtils.success(roleList);
    }

    @ApiOperation(value = "查询角色")
    @OpLog(title = "角色详情", businessType = "查询")
    @PostMapping("/get")
    @RequiresPermissions("role:query")
    public RestResult<Object> get(@RequestParam(name = "roleId") Long roleId){
        Role role = roleService.selectByPK(roleId);
        return RestResultUtils.success(role);
    }

    @ApiOperation(value = "更新角色")
    @OpLog(title = "角色管理", businessType = FixedBusinessType.UPDATE)
    @PostMapping("/update")
    @RequiresPermissions("role:edit")
    public RestResult<Object> update(@Validated({RoleDto.Update.class}) RoleDto roleDto){
        roleService.update(roleDto);
        return RestResultUtils.success();
    }

    @ApiOperation(value = "创建角色")
    @OpLog(title = "角色管理", businessType = FixedBusinessType.INSERT)
    @PostMapping("/create")
    @RequiresPermissions("role:add")
    public RestResult<Object> create(@Validated({RoleDto.Create.class}) RoleDto roleDto){
        roleService.save(roleDto);
        return RestResultUtils.success();
    }

    @ApiOperation(value = "删除角色")
    @OpLog(title = "角色管理", businessType = FixedBusinessType.DELETE)
    @PostMapping("/delete")
    @RequiresPermissions("role:del")
    public RestResult<Object> delete(@RequestParam(name = "roleId") Long roleId){
        List<Long> ids = roleService.getChildrensIdsIncludeSelf(roleId);
        if (roleService.countUser(ids) > 0) {
            return RestResultUtils.failed("无法删除存在用户的角色");
        }
        roleService.removeByIds(ids);
        return RestResultUtils.success();
    }

}