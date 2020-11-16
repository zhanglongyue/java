package com.longyue.springboot_shiro_ehcache.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.longyue.springboot_shiro_ehcache.mapper.UserMapper;
import com.longyue.springboot_shiro_ehcache.domain.User;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.longyue.springboot_shiro_ehcache.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统用户服务接口实现
 *
 * @author zly
 * @since 2020-11-16 15:35:00
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@AllArgsConstructor
@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}