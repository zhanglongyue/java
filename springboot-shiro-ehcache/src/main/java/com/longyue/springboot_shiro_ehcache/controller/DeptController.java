package com.longyue.springboot_shiro_ehcache.controller;

import com.longyue.springboot_shiro_ehcache.service.DeptService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 部门服务控制器
 *
 * @author zly
 * @since 2020-11-17 15:13:41
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/dept")
public class DeptController {
    private final DeptService deptService;

}