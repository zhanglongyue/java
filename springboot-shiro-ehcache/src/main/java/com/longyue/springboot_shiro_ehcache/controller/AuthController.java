package com.longyue.springboot_shiro_ehcache.controller;

import com.alibaba.fastjson.JSONObject;
import com.longyue.springboot_shiro_ehcache.auth.token.DefaultToken;
import com.longyue.springboot_shiro_ehcache.common.RestResponse;
import com.longyue.springboot_shiro_ehcache.domain.User;
import com.longyue.springboot_shiro_ehcache.service.UserService;
import com.longyue.springboot_shiro_ehcache.service.dto.AuthUserDto;
import com.longyue.springboot_shiro_ehcache.utils.RedisUtils;
import com.longyue.springboot_shiro_ehcache.utils.SaltUtils;
import com.longyue.springboot_shiro_ehcache.utils.TokenUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

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
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

//    @PostMapping("/login")
//    public RestResponse<Object> login(AuthUserDto authUserDto){
//        Subject subject = SecurityUtils.getSubject();
//        subject.login(new UsernamePasswordToken(authUserDto.getUsername(), authUserDto.getPassword()));
//        User user = (User) subject.getPrincipal();
//        String token = TokenUtils.getToken(user.getUserId());
//        RedisUtil.StringOps.setEx(token, JSONObject.toJSON(user).toString(), 30, TimeUnit.MINUTES);
//        return RestResponse.success(new HashMap<String, Object>() {{
//            put("token", token);
//            put("user", user);
//        }});
//    }

    @PostMapping("/login")
    public RestResponse<Object> login(AuthUserDto authUserDto){
        Subject subject = SecurityUtils.getSubject();
        subject.login(new UsernamePasswordToken(authUserDto.getUsername(), authUserDto.getPassword()));
        User user = (User) subject.getPrincipal();
        String token = TokenUtils.getToken(user.getUserId());
        RedisUtils.StringOps.setEx(token, JSONObject.toJSON(user).toString(), 30, TimeUnit.MINUTES);
        return RestResponse.success(new HashMap<String, Object>() {{
            put("token", token);
            put("user", user);
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
    public RestResponse<Object> logout(){
        SecurityUtils.getSubject().logout();
        return RestResponse.success();
    }

}