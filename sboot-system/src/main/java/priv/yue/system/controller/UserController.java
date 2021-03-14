package priv.yue.system.controller;

import cn.novelweb.tool.annotation.log.OpLog;
import cn.novelweb.tool.annotation.log.pojo.FixedBusinessType;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import priv.yue.common.base.BaseController;
import priv.yue.common.domain.User;
import priv.yue.common.dto.PageDto;
import priv.yue.system.constant.Constants;
import priv.yue.system.dto.UserDto;
import priv.yue.common.model.RestResult;
import priv.yue.common.model.RestResultUtils;
import priv.yue.system.service.UserService;
import priv.yue.common.utils.SaltUtils;
import priv.yue.system.utils.JxlsExportUtils;

import javax.servlet.http.HttpServletResponse;
import javax.validation.groups.Default;
import java.io.IOException;
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
public class UserController extends BaseController {

    private UserService userService;

    @ApiOperation("分页查询用户列表")
    @OpLog(title = "用户列表", businessType = "查询")
    @PostMapping("/list")
    @RequiresPermissions("user:query")
    @SentinelResource(value = "userList", fallback = "fallbackHandler", blockHandler = "blockHandler")
    public RestResult<Object> list(@Validated({UserDto.Query.class, Default.class}) UserDto userDto) {
        int i = 10/0;
        Map<String,Object> map = new HashMap<>();
        PageDto pager = userDto.getPager();
        map.put("search", userDto.getSearch());
        map.put("orderBy", getSortStr("user", pager.getSortBy(), pager.getSortDesc()));
        map.put("path", getUser().getDept().getPath());
        map.put("userDeptId", getUser().getDeptId());
        Page<User> page = new Page<>(pager.getPage(), pager.getItemsPerPage());
        page = userService.selectPage(page, map);
        return RestResultUtils.success(page);
    }

    public RestResult<Object> fallbackHandler(UserDto userDto , Throwable e){
        return RestResultUtils.failed(444, "异常", e.getMessage());
    }
    public RestResult<Object> blockHandler(UserDto userDto, BlockException e){
        return RestResultUtils.failed(444, "Block异常", e.getMessage());
    }

    @ApiOperation("导出用户数据")
    @OpLog(title = "导出数据", businessType = FixedBusinessType.EXPORT)
    @PostMapping("/export")
    @RequiresPermissions("user:query")
    public void export(@Validated({UserDto.Query.class, Default.class}) UserDto userDto, HttpServletResponse response) throws IOException {
        Map<String,Object> map = new HashMap<>();
        PageDto pager = userDto.getPager();
        map.put("search", userDto.getSearch());
        map.put("orderBy", getSortStr("user", pager.getSortBy(), pager.getSortDesc()));
        map.put("path", getUser().getDept().getPath());
        map.put("userDeptId", getUser().getDeptId());
        Page<User> page = new Page<>(pager.getPage(), pager.getItemsPerPage());
        page = userService.selectPage(page, map);

        Map<String, Object> data = new HashMap<>();
        data.put("items", page.getRecords());
        String templateFileName = ResourceUtils.getURL("classpath:").getPath() + "static/excel_templates/user.xlsx";
        JxlsExportUtils.doExcelExport(response, data, templateFileName, "用户信息");
    }

    /**
     * 多表关联+主从表条件+分页+一次映射
     */
    @PostMapping("/mplist")
    @RequiresPermissions("user:query")
    public RestResult<Object> mplist(@Validated({UserDto.Query.class, Default.class}) UserDto userDto) {
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
        return RestResultUtils.success(page);
    }

    @ApiOperation("根据用户id查询用户信息")
    @OpLog(title = "用户详情", businessType = "查询")
    @PostMapping("/get")
    @RequiresPermissions("user:query")
    public RestResult<Object> get(@RequestParam("userId") Long userId){
        return RestResultUtils.success(userService.selectByPK(userId));
    }

    @ApiOperation("新增用户")
    @OpLog(title = "用户管理", businessType = FixedBusinessType.INSERT)
    @PostMapping("/create")
    @RequiresPermissions("user:add")
    public RestResult<Object> create(@Validated({UserDto.Create.class, Default.class}) UserDto userDto){
        userService.save(userDto);
        return RestResultUtils.success();
    }

    @ApiOperation("更新用户")
    @OpLog(title = "用户管理", businessType = FixedBusinessType.UPDATE)
    @PostMapping("/update")
    @RequiresPermissions("user:edit")
    public RestResult<Object> update(@Validated({UserDto.Update.class, Default.class}) UserDto userDto){
        userService.update(userDto);
        return RestResultUtils.success();
    }

    @ApiOperation("删除用户")
    @OpLog(title = "用户管理", businessType = FixedBusinessType.DELETE)
    @PostMapping("/delete")
    @RequiresPermissions("user:del")
    public RestResult<Object> delete(@RequestParam("userId") Long userId){
        userService.removeById(userId);
        return RestResultUtils.success();
    }

    @ApiOperation("禁用用户")
    @OpLog(title = "禁用用户", businessType = FixedBusinessType.UPDATE)
    @PostMapping("/disable")
    @RequiresPermissions("user:edit")
    public RestResult<Object> disable(@RequestParam("userId") Long userId){
        User user = userService.getById(userId);
        userService.updateById(user.setEnabled(0));
        /**
         * 要立即生效，需要将redis中的token删除
         * todo
         */
        return RestResultUtils.success();
    }

    @ApiOperation("启用用户")
    @OpLog(title = "启用用户", businessType = FixedBusinessType.UPDATE)
    @PostMapping("/enable")
    @RequiresPermissions("user:edit")
    public RestResult<Object> enable(@RequestParam("userId") Long userId){
        User user = userService.getById(userId);
        userService.updateById(user.setEnabled(1));
        return RestResultUtils.success();
    }

    @ApiOperation("重置密码")
    @OpLog(title = "重置密码", businessType = FixedBusinessType.UPDATE)
    @PostMapping("/resetPassword")
    @RequiresPermissions("user:edit")
    public RestResult<Object> resetPassword(@RequestParam("userId") Long userId){
        User user = userService.getById(userId);
        String salt = SaltUtils.getSalt(8);
        Md5Hash hashPwd = new Md5Hash(Constants.DEFAULT_PASSWORD, salt, 1024);
        user.setPassword(hashPwd.toHex()).setSalt(salt);
        userService.updateById(user);
        return RestResultUtils.success(Constants.DEFAULT_PASSWORD);
    }

    @ApiOperation("修改密码")
    @OpLog(title = "修改密码", businessType = FixedBusinessType.UPDATE)
    @PostMapping("/changePassword")
    @RequiresPermissions("user:edit")
    public RestResult<Object> changePassword(@RequestParam("oldPassword") String oldPassword,
                                             @RequestParam("newPassword") String newPassword){
        User user = userService.getById(getUser().getUserId());
        Md5Hash hashPwd = new Md5Hash(oldPassword, user.getSalt(), 1024);
        if (hashPwd.toString().equals(user.getPassword())) {
            String salt = SaltUtils.getSalt(8);
            Md5Hash newHashPwd = new Md5Hash(newPassword, salt, 1024);
            user.setPassword(newHashPwd.toHex()).setSalt(salt);
            userService.updateById(user);
        } else {
            return RestResultUtils.failed("原密码错误");
        }
        return RestResultUtils.success("密码修改成功");
    }

    /*@PostMapping("/register")
    public RestResponse<Object> register(User user){
        String salt = SaltUtils.getSalt(8);
        Md5Hash hashPwd = new Md5Hash(user.getPassword(), salt, 1024);
        user.setPassword(hashPwd.toHex())
            .setSalt(salt)
            .setEnabled(1)
            .setCreateTime(new Date());
        userService.save(user);
        return RestResponse.success(user);
    }*/

}