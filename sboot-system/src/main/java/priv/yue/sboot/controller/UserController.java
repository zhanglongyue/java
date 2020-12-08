package priv.yue.sboot.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.RequestParam;
import priv.yue.sboot.common.RestResponse;
import priv.yue.sboot.domain.User;
import priv.yue.sboot.domain.vo.LoginVo;
import priv.yue.sboot.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 系统用户服务控制器
 *
 * @author zly
 * @since 2020-11-16 15:35:00
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @RequestMapping("/list")
    @RequiresPermissions("user:query")
    public RestResponse<Object> list(User user, @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                     @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                     HttpServletRequest req){
        IPage<User> page = new Page<>(pageNo, pageSize);
        page = userService.page(page);
        return RestResponse.success(page);
    }

    @RequestMapping("/get")
    @RequiresPermissions("user:query")
    public RestResponse<Object> getUser(Integer id){
        return RestResponse.success(userService.getUserById(id));
    }
}