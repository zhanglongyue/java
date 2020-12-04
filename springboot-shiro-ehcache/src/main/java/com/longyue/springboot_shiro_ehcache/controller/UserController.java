package com.longyue.springboot_shiro_ehcache.controller;

import com.longyue.springboot_shiro_ehcache.common.RestResponse;
import com.longyue.springboot_shiro_ehcache.domain.User;
import com.longyue.springboot_shiro_ehcache.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @RequiresPermissions("user:list")
    public RestResponse<Object> list(){
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        System.out.println(user);
        return RestResponse.success(userService.list());
    }

    @RequestMapping("/get")
    @RequiresPermissions("user:list")
    public RestResponse<Object> getUser(Integer id){
        return RestResponse.success(userService.getUserById(id));
    }
}