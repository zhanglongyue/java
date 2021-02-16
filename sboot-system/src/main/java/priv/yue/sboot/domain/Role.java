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
 * 角色表(sys_role)实体类
 *
 * @author ZhangLongYue
 * @since 2020-11-17 15:07:40
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("sys_role")
public class Role extends Model<Role> implements Serializable {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
	private Long roleId;
    /**
     * 名称
     */
    private String name;
    /**
     * 父角色id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long pid;
    /**
     * 描述
     */
    private String description;
    /**
     * 数据权限
     */
    private String dataScope;
    /**
     * 创建者
     */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;
    /**
     * 更新者
     */
    @TableField(fill = FieldFill.UPDATE)
    private String updateBy;
    /**
     * 创建日期
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.UPDATE)
	private Date updateTime;
    /**
     * 是否删除
     */
    @JsonIgnore
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;
    /**
     * 用户集合
     */
    @JsonIgnore
    @TableField(exist = false)
    private List<User> users;
    /**
     * 上级角色或分类
     */
    @TableField(exist = false)
    private Role parentRole;
    /**
     * 子角色集合
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @TableField(exist = false)
    private List<Role> subRole;
    /**
     * 资源集合
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @TableField(exist = false)
    private List<Menu> menus;
}