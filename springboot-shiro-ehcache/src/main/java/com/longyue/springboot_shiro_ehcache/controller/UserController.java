package com.longyue.springboot_shiro_ehcache.controller;

import com.longyue.springboot_shiro_ehcache.domain.User;
import com.longyue.springboot_shiro_ehcache.service.UserService;
import com.longyue.springboot_shiro_ehcache.utils.SaltUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @RequestMapping({"/","/index"})
    public String index(){
        return "index";
    }

    @RequestMapping("/login")
    public String login(String username, String password, Model model){
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(new UsernamePasswordToken(username, password));
            return "index";
        } catch (UnknownAccountException e) {
            System.out.println("用户名错误!");
        } catch (IncorrectCredentialsException e){
            System.out.println("密码错误!");
        }
        return "auth/login";
    }

    @RequestMapping("/register")
    public String register(User user, Model model){
        String salt = SaltUtils.getSalt(8);
        user.setSalt(salt);
        Md5Hash hashPwd = new Md5Hash(user.getPassword(), salt, 1024);
        user.setPassword(hashPwd.toHex());
        userService.save(user);
        return "auth/login";
    }

    @RequestMapping("/goRegister")
    public String goRegister(){
        return "auth/register";
    }

}