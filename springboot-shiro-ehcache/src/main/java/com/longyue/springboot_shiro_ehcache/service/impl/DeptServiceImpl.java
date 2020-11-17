package com.longyue.springboot_shiro_ehcache.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.longyue.springboot_shiro_ehcache.domain.Dept;
import com.longyue.springboot_shiro_ehcache.mapper.DeptMapper;
import com.longyue.springboot_shiro_ehcache.service.DeptService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 部门服务接口实现
 *
 * @author zly
 * @since 2020-11-17 15:13:41
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@AllArgsConstructor
@Service
@Transactional
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {

}