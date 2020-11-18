package com.longyue.springboot_shiro_ehcache.domain;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 系统用户(sys_user)实体类
 *
 * @author zly
 * @since 2020-11-16 15:35:00
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("sys_user")
public class User extends Model<User> implements Serializable {
    /**
     * ID
     */
    @TableId
	private Long userId;
    /**
     * 部门名称
     */
    private Long deptId;
    /**
     * 用户名
     */
    private String username;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 性别
     */
    private String gender;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 头像地址
     */
    private String avatarName;
    /**
     * 头像真实路径
     */
    private String avatarPath;
    /**
     * 密码
     */
    private String password;
    /**
     * 是否为admin账号
     */
    private Integer isAdmin;
    /**
     * 状态：1启用、0禁用
     */
    private Long enabled;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 更新者
     */
    private String updateBy;
    /**
     * 修改密码的时间
     */
    private Date pwdResetTime;
    /**
     * 创建日期
     */
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField(update = "now()")
	private Date updateTime;
    /**
     * 盐
     */
    private String salt;

    @TableField(exist = false)
    private List<Role> roles;

    @TableField(exist = false)
    private List<Menu> menus;

}