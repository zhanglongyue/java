package priv.yue.sboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import priv.yue.sboot.common.RestResponse;
import priv.yue.sboot.domain.Dept;
import priv.yue.sboot.domain.Menu;
import priv.yue.sboot.domain.User;
import priv.yue.sboot.domain.maps.DeptMap;
import priv.yue.sboot.domain.maps.MenuMap;
import priv.yue.sboot.service.dto.DeptDto;
import priv.yue.sboot.service.dto.MenuDto;
import priv.yue.sboot.vo.LoginVo;
import priv.yue.sboot.service.MenuService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统菜单服务控制器
 *
 * @author ZhangLongYue
 * @since 2020-11-17 15:17:03
 * @description
 */
@Api(tags = "菜单管理")
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/menu")
public class MenuController extends BaseController{

    private final MenuService menuService;
    private MenuMap menuMap;

    @ApiOperation(value = "根据菜单名称查询菜单列表", notes = "只能查询当前用户所拥有的菜单")
    @PostMapping("/list")
    @RequiresPermissions("menu:query")
    public RestResponse<Object> list(){
        List<Menu> menuList = menuService.selectTreeByUser(getUser().getUserId());
        return RestResponse.success(menuList);
    }

    @ApiOperation(value = "获取前端路由")
    @PostMapping("/getRouters")
    public RestResponse<Object> getRouters(){
        List<Menu> menuList = menuService.selectByUser(getUser().getUserId(), 0, new Integer[]{1} );
        return RestResponse.success(menuList);
    }

    @ApiOperation("根据主键查询菜单")
    @PostMapping("/get")
    @RequiresPermissions("menu:query")
    public RestResponse<Object> get(Long menuId){
        Menu menu = menuService.selectByPrimaryKey(menuId);
        return RestResponse.success(menu);
    }

    @ApiOperation("新增菜单")
    @PostMapping("/create")
    @RequiresPermissions("menu:add")
    public RestResponse<Object> create(@Validated({MenuDto.Create.class}) MenuDto menuDto){
        Menu menu = menuMap.toEntity(menuDto);
        menuService.save(menu);
        return RestResponse.success();
    }

    @ApiOperation("修改菜单")
    @PostMapping("/update")
    @RequiresPermissions("menu:edit")
    public RestResponse<Object> update(@Validated({MenuDto.Update.class}) MenuDto menuDto){
        Menu menu = menuMap.toEntity(menuDto);
        menuService.updateById(menu);
        return RestResponse.success();
    }

    @ApiOperation("删除菜单")
    @PostMapping("/delete")
    @RequiresPermissions("menu:del")
    public RestResponse<Object> delete(Long menuId){
        List<Long> ids = menuService.getMenuAndChildrensIds(menuId);
        menuService.removeByIds(ids);
        return RestResponse.success();
    }


}