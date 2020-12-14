package priv.yue.sboot.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.bind.annotation.GetMapping;
import priv.yue.sboot.common.RestResponse;
import priv.yue.sboot.domain.Menu;
import priv.yue.sboot.domain.User;
import priv.yue.sboot.domain.vo.LoginVo;
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
public class MenuController {

    private final MenuService menuService;

    @ApiOperation(value = "根据菜单名称查询菜单列表", notes = "只能查询当前用户所拥有的菜单")
    @GetMapping("/list")
    public RestResponse<Object> list(String title){
        User user = ((LoginVo) SecurityUtils.getSubject().getPrincipal()).getUser();
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("userId", user.getUserId());
        queryMap.put("title", title);
        List<Menu> menuList = menuService.queryMenusWithUser(queryMap);
        return RestResponse.success(menuList);
    }


}