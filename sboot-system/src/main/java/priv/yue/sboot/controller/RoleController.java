package priv.yue.sboot.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import priv.yue.sboot.common.RestResponse;
import priv.yue.sboot.domain.Role;
import priv.yue.sboot.service.RoleService;

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
public class RoleController extends BaseController{
    private final RoleService roleService;

    @ApiOperation(value = "查询角色树", notes = "只能查询当前用户角色及子角色")
    @PostMapping("/list")
    @RequiresPermissions("role:query")
    public RestResponse<Object> list(){
        List<Role> roleList = roleService.selectRolesTreeByUser(getUser().getUserId());
        return RestResponse.success(roleList);
    }

}