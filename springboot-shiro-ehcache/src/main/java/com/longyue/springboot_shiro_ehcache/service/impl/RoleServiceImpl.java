package com.longyue.springboot_shiro_ehcache.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.longyue.springboot_shiro_ehcache.domain.Role;
import com.longyue.springboot_shiro_ehcache.mapper.RoleMapper;
import com.longyue.springboot_shiro_ehcache.service.RoleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 角色表服务接口实现
 *
 * @author zly
 * @since 2020-11-17 15:07:40
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@AllArgsConstructor
@Service
@Transactional
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

}