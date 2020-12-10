package priv.yue.sboot.controller;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import priv.yue.sboot.common.RestResponse;
import priv.yue.sboot.common.constant.Consts;
import priv.yue.sboot.config.SbootConfig;
import priv.yue.sboot.domain.User;
import priv.yue.sboot.domain.vo.LoginVo;
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

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class LoginController {

    private SbootConfig sbootConfig;

    private UserService userService;

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
                30, TimeUnit.MINUTES);

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
            RedisUtils.StringOps.setEx(tokenKey, newToken, 30, TimeUnit.MINUTES);
        }
        return RestResponse.success(new HashMap<String, Object>() {{
            put("token", newToken);
            put("user", loginVo.getUser());
            put("roles", loginVo.getRoles());
            put("menus", loginVo.getMenus());
            put("permissions", loginVo.getPermissions());
        }});
    }

    @PostMapping("/register")
    public RestResponse<Object> register(User user){
        String salt = SaltUtils.getSalt(8);
        user.setSalt(salt);
        Md5Hash hashPwd = new Md5Hash(user.getPassword(), salt, 1024);
        user.setPassword(hashPwd.toHex());
        user.setEnabled(1);
        user.setCreateTime(new Date());
        userService.save(user);
        return RestResponse.success(new HashMap<String, Object>() {{
            put("user", user);
        }});
    }

    @PostMapping("/logout")
    public RestResponse<Object> logout(HttpServletRequest request){
        User user = ((LoginVo) SecurityUtils.getSubject().getPrincipal()).getUser();
        String token = request.getHeader("token");
        if (RedisUtils.KeyOps.delete(Consts.SHIRO_TOKEN_PREFIX + token)) {
            if (sbootConfig.getSingleLogin()) {
                RedisUtils.KeyOps.delete(Consts.SHIRO_USER_PREFIX + user.getUserId());
            }
        }
        return RestResponse.success();
    }
}