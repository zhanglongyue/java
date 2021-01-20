package priv.yue.sboot.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 系统菜单(sys_menu)实体类
 *
 * @author ZhangLongYue
 * @since 2020-11-17 15:17:03
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("sys_menu")
public class Menu extends Model<Menu> implements Serializable {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
	private Long menuId;
    /**
     * 上级菜单ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long pid;
    /**
     * 菜单类型 0-菜单 1-页面 2-按钮
     */
    private Integer type;
    /**
     * 菜单标题
     */
    private String title;
    /**
     * 组件名称
     */
    private String name;
    /**
     * 组件
     */
    private String component;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 图标
     */
    private String icon;
    /**
     * 链接地址
     */
    private String path;
    /**
     * 是否外链
     */
    private Integer iframe;
    /**
     * 缓存
     */
    private Integer cache;
    /**
     * 隐藏
     */
    private Integer hidden;
    /**
     * 权限
     */
    private String permission;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 更新者
     */
    private String updateBy;
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
     * 是否删除
     */
    @JsonIgnore
    @TableLogic
    private Integer deleted;
    /**
     * 上级菜单
     */
    @TableField(exist = false)
    private Menu parentMenu;
    /**
     * 子菜单集合
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @TableField(exist = false)
    private List<Menu> subMenu;

}