package priv.yue.sboot.controller;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import priv.yue.sboot.common.RestResponse;
import priv.yue.sboot.common.constant.Consts;
import priv.yue.sboot.config.SbootConfig;
import priv.yue.sboot.domain.User;
import priv.yue.sboot.vo.LoginVo;
import priv.yue.sboot.service.UserService;
import priv.yue.sboot.service.dto.AuthUserDto;
import priv.yue.sboot.utils.JsonUtils;
import priv.yue.sboot.utils.RedisUtils;
import priv.yue.sboot.utils.SaltUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 系统认证控制器
 *
 * @author ZhangLongYue
 * @since 2020-11-17 15:17:03
 * @description
 */
@Api(tags = "认证")
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class LoginController extends BaseController{

    private SbootConfig sbootConfig;

    private UserService userService;

    @ApiOperation("登录")
    @PostMapping("/login")
    public RestResponse<Object> login(AuthUserDto authUserDto){
        // 获取suject主体，并使用用户名、密码方式登录
        Subject subject = SecurityUtils.getSubject();
        subject.login(new UsernamePasswordToken(authUserDto.getUsername(), authUserDto.getPassword()));

        // 获取登录后信息
        LoginVo loginVo = (LoginVo) subject.getPrincipal();

        // 保存token及登录信息
        String newToken = UUID.randomUUID().toString();
        RedisUtils.StringOps.setEx(Consts.SHIRO_TOKEN_PREFIX + newToken, JsonUtils.toJsonString(loginVo),
                sbootConfig.getDefaultTokenTimeout(), TimeUnit.MILLISECONDS);

        // 单一登录处理
        if (sbootConfig.getSingleLogin()) {
            // 单一登录以用户id为key，存放唯一token
            String tokenKey = Consts.SHIRO_USER_PREFIX + loginVo.getUser().getUserId();
            // 获取用户token
            String oldToken = Consts.SHIRO_TOKEN_PREFIX + RedisUtils.StringOps.get(tokenKey);
            if (StrUtil.isNotEmpty(oldToken)) {
                // 删除旧token
                RedisUtils.KeyOps.delete(oldToken);
            }
            // 给用户设置一个新token
            RedisUtils.StringOps.setEx(tokenKey, newToken, sbootConfig.getDefaultTokenTimeout(), TimeUnit.MILLISECONDS);
        }
        return RestResponse.success(new HashMap<String, Object>() {{
            put("token", newToken);
            put("user", loginVo.getUser());
            put("roles", loginVo.getRoles());
            put("menus", loginVo.getMenus());
            put("permissions", loginVo.getPermissions());
        }});
    }

    @ApiOperation(value = "获取当前登录用户信息")
    @PostMapping("/getLoginInfo")
    public RestResponse<Object> getLoginInfo(){
        return RestResponse.success(getLoginVo());
    }

    @PostMapping("/register")
    public RestResponse<Object> register(User user){
        String salt = SaltUtils.getSalt(8);
        Md5Hash hashPwd = new Md5Hash(user.getPassword(), salt, 1024);
        user.setPassword(hashPwd.toHex())
            .setSalt(salt)
            .setEnabled(1)
            .setCreateTime(new Date());
        userService.save(user);
        return RestResponse.success(user);
    }

    @ApiOperation("登出")
    @PostMapping("/logout")
    public RestResponse<Object> logout(HttpServletRequest request){
        String token = request.getHeader("Access-Token");
        if (RedisUtils.KeyOps.delete(Consts.SHIRO_TOKEN_PREFIX + token)) {
            if (sbootConfig.getSingleLogin()) {
                RedisUtils.KeyOps.delete(Consts.SHIRO_USER_PREFIX + getUser().getUserId());
            }
        }
        return RestResponse.success();
    }
}