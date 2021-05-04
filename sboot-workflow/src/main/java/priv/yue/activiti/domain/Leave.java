package priv.yue.activiti.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 请假单实体类
 *
 * @author ZhangLongYue
 * @since 2021-05-02 15:13:41
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("wf_leave")
public class Leave implements Serializable {

    /**
     * 
     */
    @TableId(type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    /**
     * 所属部门
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long deptId;

    /**
     * 流程实例id
     */
    private String procInstId;

    /**
     * 用户id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    /**
     * 请假人姓名
     */
    private String username;

    /**
     * 请假原因
     */
    private String reason;

    /**
     * 请假开始时间
     */
    private Date startTime;

    /**
     * 请假结束时间
     */
    private Date endTime;

    /**
     * 请假天数
     */
    private Integer days;

    /**
     * 请假小时数
     */
    private Integer hours;

    /**
     * 流程状态
     */
//    @TableField(exist = false)
    private String status;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.UPDATE)
    private String updateBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

}