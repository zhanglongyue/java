package priv.yue.sboot.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;
import priv.yue.sboot.common.RestResponse;
import priv.yue.sboot.domain.User;
import priv.yue.sboot.service.UserService;

import javax.servlet.http.HttpServletRequest;

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
public class UserController {
    private final UserService userService;

    @ApiOperation("分页查询用户列表")
    @GetMapping("/list")
    @RequiresPermissions("user:query")
    public RestResponse<Object> list(User user, @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                     @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                     HttpServletRequest req){
        IPage<User> page = new Page<>(pageNo, pageSize);
        page = userService.page(page);
        return RestResponse.success(page);
    }

    @ApiOperation("根据用户id查询用户信息")
    @GetMapping("/get")
    @RequiresPermissions("user:query")
    public RestResponse<Object> getUser(long id){
        return RestResponse.success(userService.getUserById(id));
    }

    @ApiOperation("新增用户")
    @PostMapping("/create")
    @RequiresPermissions("user:query")
    public RestResponse<Object> createUser(User user){
        return RestResponse.success(userService.save(user));
    }
}