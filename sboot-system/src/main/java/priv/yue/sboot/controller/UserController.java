package priv.yue.sboot.controller;

import cn.novelweb.tool.annotation.log.OpLog;
import cn.novelweb.tool.annotation.log.pojo.FixedBusinessType;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import priv.yue.sboot.common.RestResponse;
import priv.yue.sboot.config.SbootConfig;
import priv.yue.sboot.domain.User;
import priv.yue.sboot.service.UserService;
import priv.yue.sboot.service.dto.BaseDto;
import priv.yue.sboot.service.dto.PageDto;
import priv.yue.sboot.service.dto.UserDto;
import priv.yue.sboot.utils.SaltUtils;

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

    private SbootConfig sbootConfig;

    private UserService userService;

    @ApiOperation("分页查询用户列表")
    @OpLog(title = "用户列表", businessType = "查询")
    @PostMapping("/list")
    @RequiresPermissions("user:query")
    public RestResponse<Object> list(@Validated({UserDto.Query.class, Default.class}) UserDto userDto) {
        Map<String,Object> map = new HashMap<>();
        PageDto pager = userDto.getPager();
        map.put("search", userDto.getSearch());
        map.put("orderBy", getSortStr("user", pager.getSortBy(), pager.getSortDesc()));
        map.put("path", getUser().getDept().getPath());
        map.put("userDeptId", getUser().getDeptId());
        Page<User> page = new Page<>(pager.getPage(), pager.getItemsPerPage());
        page = userService.selectPage(page, map);
        return RestResponse.success(page);
    }

    /**
     * 多表关联+主从表条件+分页+一次映射
     */
    @PostMapping("/mplist")
    @RequiresPermissions("user:query")
    public RestResponse<Object> mplist(@Validated({BaseDto.Query.class, Default.class}) UserDto userDto) {
        Map<String,Object> map = new HashMap<>();
        PageDto pager = userDto.getPager();
        map.put("search", userDto.getSearch());
        map.put("orderBy", getSortStr("user", pager.getSortBy(), pager.getSortDesc()));
        map.put("path", getUser().getDept().getPath());
        map.put("userDeptId", getUser().getDeptId());
        map.put("limitBegin", (pager.getPage()-1) * pager.getItemsPerPage());
        map.put("limitEnd", pager.getPage() * pager.getItemsPerPage());
        Page<User> page = new Page<>(pager.getPage(), pager.getItemsPerPage());
        page.setSearchCount(false);
        page.setTotal(userService.selectPageCount(map));
        page.setRecords(userService.selectPageOnce(map));
        return RestResponse.success(page);
    }

    @ApiOperation("根据用户id查询用户信息")
    @OpLog(title = "用户详情", businessType = "查询")
    @PostMapping("/get")
    @RequiresPermissions("user:query")
    public RestResponse<Object> get(@RequestParam("userId") Long userId){
        return RestResponse.success(userService.selectByPK(userId));
    }

    @ApiOperation("新增用户")
    @OpLog(title = "用户管理", businessType = FixedBusinessType.INSERT)
    @PostMapping("/create")
    @RequiresPermissions("user:add")
    public RestResponse<Object> create(@Validated({UserDto.Create.class, Default.class}) UserDto userDto){
        userService.save(userDto);
        return RestResponse.success();
    }

    @ApiOperation("更新用户")
    @OpLog(title = "用户管理", businessType = FixedBusinessType.UPDATE)
    @PostMapping("/update")
    @RequiresPermissions("user:edit")
    public RestResponse<Object> update(@Validated({UserDto.Update.class, Default.class}) UserDto userDto){
        userService.update(userDto);
        return RestResponse.success();
    }

    @ApiOperation("删除用户")
    @OpLog(title = "用户管理", businessType = FixedBusinessType.DELETE)
    @PostMapping("/delete")
    @RequiresPermissions("user:del")
    public RestResponse<Object> delete(@RequestParam("userId") Long userId){
        userService.removeById(userId);
        return RestResponse.success();
    }

    @ApiOperation("禁用用户")
    @OpLog(title = "禁用用户", businessType = FixedBusinessType.UPDATE)
    @PostMapping("/disable")
    @RequiresPermissions("user:edit")
    public RestResponse<Object> disable(@RequestParam("userId") Long userId){
        User user = userService.getById(userId);
        userService.updateById(user.setEnabled(0));
        /**
         * 如果要立即生效，可以将redis中的token删除
         * todo
         */
        return RestResponse.success();
    }

    @ApiOperation("启用用户")
    @OpLog(title = "启用用户", businessType = FixedBusinessType.UPDATE)
    @PostMapping("/enable")
    @RequiresPermissions("user:edit")
    public RestResponse<Object> enable(@RequestParam("userId") Long userId){
        User user = userService.getById(userId);
        userService.updateById(user.setEnabled(1));
        return RestResponse.success();
    }

    @ApiOperation("重置密码")
    @OpLog(title = "重置密码", businessType = FixedBusinessType.UPDATE)
    @PostMapping("/resetPassword")
    @RequiresPermissions("user:edit")
    public RestResponse<Object> resetPassword(@RequestParam("userId") Long userId){
        User user = userService.getById(userId);
        String salt = SaltUtils.getSalt(8);
        Md5Hash hashPwd = new Md5Hash(sbootConfig.getDefaultPassword(), salt, 1024);
        user.setPassword(hashPwd.toHex()).setSalt(salt);
        userService.updateById(user);
        return RestResponse.success(sbootConfig.getDefaultPassword());
    }

    @ApiOperation("修改密码")
    @OpLog(title = "修改密码", businessType = FixedBusinessType.UPDATE)
    @PostMapping("/changePassword")
    @RequiresPermissions("user:edit")
    public RestResponse<Object> changePassword(@RequestParam("oldPassword") String oldPassword,
                                               @RequestParam("newPassword") String newPassword){
        User user = userService.getById(getUser().getUserId());
        Md5Hash hashPwd = new Md5Hash(oldPassword, user.getSalt(), 1024);
        if (hashPwd.toString().equals(user.getPassword())) {
            String salt = SaltUtils.getSalt(8);
            Md5Hash newHashPwd = new Md5Hash(newPassword, salt, 1024);
            user.setPassword(newHashPwd.toHex()).setSalt(salt);
            userService.updateById(user);
        } else {
            return RestResponse.fail("原密码错误");
        }
        return RestResponse.success("密码修改成功");
    }

}