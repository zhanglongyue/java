package com.longyue.springboot_shiro_ehcache.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.longyue.springboot_shiro_ehcache.domain.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 系统菜单(sys_menu)数据Mapper
 *
 * @author zly
 * @since 2020-11-17 15:17:03
 * @description 由 Mybatisplus Code Generator 创建
*/
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {
    List<Menu> getAllSubMenu(Integer pid);

    List<Menu> getRoleMenu(Integer roleId);
}
