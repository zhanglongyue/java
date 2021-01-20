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
import priv.yue.sboot.domain.User;
import priv.yue.sboot.service.UserService;
import priv.yue.sboot.service.dto.BaseDto;
import priv.yue.sboot.service.dto.UserDto;

import javax.validation.groups.Default;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统用户服务控制器
 *
 * @author ZhangLongYue
 * @since 2020-11-17 15:17:03
 * @description
 */
@Api(tags = "用户管理")
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController extends BaseController{

    private UserService userService;

    @ApiOperation("分页查询用户列表")
    @PostMapping("/list")
    @RequiresPermissions("user:query")
    public RestResponse<Object> list(@Validated({BaseDto.Query.class, Default.class}) UserDto userDto) {
        Map<String,Object> map = new HashMap<>();
        map.put("search", userDto.getSearch());
        map.put("orderBy", getSortStr("user", userDto.getSortBy(), userDto.getSortDesc()));
        map.put("path", getUser().getDept().getPath());
        map.put("userDeptId", getUser().getDeptId());
        Page<User> page = new Page<>(userDto.getPage(), userDto.getItemsPerPage());
        page = userService.selectByUser(page, map);
        return RestResponse.success(page);
    }

    /**
     * 多表关联+主从表条件+分页+一次映射
     */
    @PostMapping("/mplist")
    @RequiresPermissions("user:query")
    public RestResponse<Object> mplist(@Validated({BaseDto.Query.class, Default.class}) UserDto userDto) {
        Map<String,Object> map = new HashMap<>();
        map.put("search", userDto.getSearch());
        map.put("orderBy", getSortStr("user", userDto.getSortBy(), userDto.getSortDesc()));
        map.put("path", getUser().getDept().getPath());
        map.put("userDeptId", getUser().getDeptId());
        map.put("limitBegin", (userDto.getPage()-1) * userDto.getItemsPerPage());
        map.put("limitEnd", userDto.getPage() * userDto.getItemsPerPage());
        Page<User> page = new Page<>(userDto.getPage(), userDto.getItemsPerPage());
        page.setSearchCount(false);
        page.setTotal(userService.selectByUserCount(map));
        page.setRecords(userService.selectByUserOnce(map));
        return RestResponse.success(page);
    }

    @ApiOperation("根据用户id查询用户信息")
    @PostMapping("/get")
    @RequiresPermissions("user:query")
    public RestResponse<Object> get(Long userId){
        return RestResponse.success(userService.getUserById(userId));
    }

    @ApiOperation("新增用户")
    @PostMapping("/create")
    @RequiresPermissions("user:add")
    public RestResponse<Object> create(@Validated({UserDto.Create.class, Default.class}) UserDto userDto){
        userService.insertUser(userDto);
        return RestResponse.success();
    }

    @ApiOperation("更新用户")
    @PostMapping("/update")
    @RequiresPermissions("user:edit")
    public RestResponse<Object> update(@Validated({UserDto.Update.class, Default.class}) UserDto userDto){
        userService.updateUser(userDto);
        return RestResponse.success();
    }

    @ApiOperation("删除用户")
    @PostMapping("/delete")
    @RequiresPermissions("user:del")
    public RestResponse<Object> delete(Long userId){
        userService.removeById(userId);
        return RestResponse.success();
    }

    @ApiOperation("禁用用户")
    @PostMapping("/disable")
    @RequiresPermissions("user:edit")
    public RestResponse<Object> disable(Long userId){
        User user = userService.getById(userId);
        userService.updateById(user.setEnabled(0));
        /**
         * 如果要立即生效，可以将redis中的token删除
         * todo
         */
        return RestResponse.success();
    }

    @ApiOperation("启用用户")
    @PostMapping("/enable")
    @RequiresPermissions("user:edit")
    public RestResponse<Object> enable(Long userId){
        User user = userService.getById(userId);
        userService.updateById(user.setEnabled(1));
        return RestResponse.success();
    }

}