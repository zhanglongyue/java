package priv.yue.system.controller;

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
import priv.yue.common.base.BaseController;
import priv.yue.common.domain.Menu;
import priv.yue.common.model.RestResult;
import priv.yue.system.domain.maps.MenuMap;
import priv.yue.common.model.RestResultUtils;
import priv.yue.system.service.MenuService;
import priv.yue.system.dto.MenuDto;

import java.util.List;

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
public class MenuController extends BaseController {

    private final MenuService menuService;
    private MenuMap menuMap;

    @ApiOperation(value = "根据菜单名称查询菜单列表", notes = "只能查询当前用户所拥有的菜单")
    @OpLog(title = "菜单树", businessType = "查询")
    @PostMapping("/list")
    @RequiresPermissions({"menu:query", "role:query"})
    public RestResult<Object> list(){
        if (getUser().getUsername().equals("admin")) {
            return RestResultUtils.success(menuService.buildTree(menuService.list(
                    new QueryWrapper<Menu>().orderByAsc("sort")))
            );
        }
        List<Menu> menuList = menuService.selectTreeByUser(getUser().getUserId());
        return RestResultUtils.success(menuList);
    }

    @ApiOperation(value = "获取前端路由")
    @PostMapping("/getRouters")
    public RestResult<Object> getRouters(){
        List<Menu> menuList = menuService.selectByUser(getUser().getUserId(), 0, new Integer[]{1} );
        return RestResultUtils.success(menuList);
    }

    @ApiOperation("根据主键查询菜单")
    @OpLog(title = "菜单详情", businessType = "查询")
    @PostMapping("/get")
    @RequiresPermissions("menu:query")
    public RestResult<Object> get(@RequestParam(name = "menuId") Long menuId){
        Menu menu = menuService.selectByPK(menuId);
        return RestResultUtils.success(menu);
    }

    @ApiOperation("新增菜单")
    @OpLog(title = "菜单管理", businessType = FixedBusinessType.INSERT)
    @PostMapping("/create")
    @RequiresPermissions("menu:add")
    public RestResult<Object> create(@Validated({MenuDto.Create.class}) MenuDto menuDto){
        Menu menu = menuMap.toEntity(menuDto);
        menuService.save(menu);
        return RestResultUtils.success();
    }

    @ApiOperation("更新菜单")
    @OpLog(title = "菜单管理", businessType = FixedBusinessType.UPDATE)
    @PostMapping("/update")
    @RequiresPermissions("menu:edit")
    public RestResult<Object> update(@Validated({MenuDto.Update.class}) MenuDto menuDto){
        Menu menu = menuMap.toEntity(menuDto);
        menuService.updateById(menu);
        return RestResultUtils.success();
    }

    @ApiOperation("删除菜单")
    @OpLog(title = "菜单管理", businessType = FixedBusinessType.DELETE)
    @PostMapping("/delete")
    @RequiresPermissions("menu:del")
    public RestResult<Object> delete(@RequestParam(name = "menuId") Long menuId){
        List<Long> ids = menuService.getChildrensIdsIncludeSelf(menuId);
        menuService.removeByIds(ids);
        return RestResultUtils.success();
    }


}