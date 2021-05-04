package priv.yue.common.domain;

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
 * 部门(sys_dept)实体类
 *
 * @author ZhangLongYue
 * @since 2020-11-17 15:13:41
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("sys_dept")
public class Dept extends Model<Dept> implements Serializable {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
	private Long deptId;
    /**
     * 上级部门
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long pid;
    /**
     * 树形路径
     */
    private String path;
    /**
     * 名称
     */
    private String name;
    /**
     * 排序
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer sort;
    /**
     * 状态
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer enabled;
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
     * 上级部门
     */
    @TableField(exist = false)
    private Dept parentDept;
    /**
     * 子部门集合
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @TableField(exist = false)
    private List<Dept> subDept;

}