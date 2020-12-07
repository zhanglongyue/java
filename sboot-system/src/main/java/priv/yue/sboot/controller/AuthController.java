package priv.yue.sboot.controller;

import priv.yue.sboot.common.Consts;
import priv.yue.sboot.common.RestResponse;
import priv.yue.sboot.domain.User;
import priv.yue.sboot.domain.vo.LoginVo;
import priv.yue.sboot.service.UserService;
import priv.yue.sboot.service.dto.AuthUserDto;
import priv.yue.sboot.utils.JsonUtils;
import priv.yue.sboot.utils.RedisUtils;
import priv.yue.sboot.utils.SaltUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public RestResponse<Object> login(AuthUserDto authUserDto){
        Subject subject = SecurityUtils.getSubject();
        subject.login(new UsernamePasswordToken(authUserDto.getUsername(), authUserDto.getPassword()));
        LoginVo loginVo = (LoginVo) subject.getPrincipal();
        String token = UUID.randomUUID().toString();
        RedisUtils.StringOps.setEx(Consts.TOKEN_PREFIX + token, JsonUtils.toJsonString(loginVo),
                30, TimeUnit.MINUTES);
        return RestResponse.success(new HashMap<String, Object>() {{
            put("token", token);
            put("user", loginVo.getUser());
            put("roles", loginVo.getRoles());
            put("menus", loginVo.getMenus());
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
    public RestResponse<Object> logout(ServletRequest request){
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String token = httpServletRequest.getHeader("token");
        RedisUtils.KeyOps.delete(Consts.TOKEN_PREFIX + token);
        return RestResponse.success();
    }

}