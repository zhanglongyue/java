package priv.yue.sboot.domain;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 部门(sys_dept)实体类
 *
 * @author zly
 * @since 2020-11-17 15:13:41
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("sys_dept")
public class Dept extends Model<Dept> implements Serializable {
    private static final long serialVersionUID = 6458696231145940196L;
    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
	private Long deptId;
    /**
     * 上级部门
     */
    private Long pid;
    /**
     * 子部门数目
     */
    private Integer subCount;
    /**
     * 名称
     */
    private String name;
    /**
     * 排序
     */
    private Integer deptSort;
    /**
     * 状态
     */
    private Integer enabled;
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

}