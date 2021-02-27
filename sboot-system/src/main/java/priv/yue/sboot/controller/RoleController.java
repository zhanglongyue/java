package priv.yue.sboot.controller;


import cn.novelweb.tool.annotation.log.OpLog;
import cn.novelweb.tool.annotation.log.pojo.FixedBusinessType;
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
import priv.yue.sboot.domain.Role;
import priv.yue.sboot.domain.maps.RoleMap;
import priv.yue.sboot.service.RoleService;
import priv.yue.sboot.dto.RoleDto;

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
    public RestResponse<Object> list(){
        List<Role> roleList = roleService.selectTreeByUser(getUser().getUserId());
        return RestResponse.success(roleList);
    }

    @ApiOperation(value = "查询角色")
    @OpLog(title = "角色详情", businessType = "查询")
    @PostMapping("/get")
    @RequiresPermissions("role:query")
    public RestResponse<Object> get(@RequestParam(name = "roleId") Long roleId){
        Role role = roleService.selectByPK(roleId);
        return RestResponse.success(role);
    }

    @ApiOperation(value = "更新角色")
    @OpLog(title = "角色管理", businessType = FixedBusinessType.UPDATE)
    @PostMapping("/update")
    @RequiresPermissions("role:edit")
    public RestResponse<Object> update(@Validated({RoleDto.Update.class}) RoleDto roleDto){
        roleService.update(roleDto);
        return RestResponse.success();
    }

    @ApiOperation(value = "创建角色")
    @OpLog(title = "角色管理", businessType = FixedBusinessType.INSERT)
    @PostMapping("/create")
    @RequiresPermissions("role:add")
    public RestResponse<Object> create(@Validated({RoleDto.Create.class}) RoleDto roleDto){
        roleService.save(roleDto);
        return RestResponse.success();
    }

    @ApiOperation(value = "删除角色")
    @OpLog(title = "角色管理", businessType = FixedBusinessType.DELETE)
    @PostMapping("/delete")
    @RequiresPermissions("role:del")
    public RestResponse<Object> delete(@RequestParam(name = "roleId") Long roleId){
        List<Long> ids = roleService.getChildrensIdsIncludeSelf(roleId);
        if (roleService.countUser(ids) > 0) {
            return RestResponse.fail("无法删除存在用户的角色");
        }
        roleService.removeByIds(ids);
        return RestResponse.success();
    }

}