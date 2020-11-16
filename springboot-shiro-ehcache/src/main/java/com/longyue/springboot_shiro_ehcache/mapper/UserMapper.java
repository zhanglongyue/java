package com.longyue.springboot_shiro_ehcache.mapper;

import com.longyue.springboot_shiro_ehcache.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统用户(sys_user)数据Mapper
 *
 * @author zly
 * @since 2020-11-16 15:35:00
 * @description 由 Mybatisplus Code Generator 创建
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
