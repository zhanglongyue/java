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
 * 
 * @TableName wf_leave_flow
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("wf_leave_flow")
public class LeaveFlow implements Serializable {
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
     * actviit任务id
     */
    private String taskId;

    /**
     * 用户id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    /**
     * 审批结果
     */
    private String result;

    /**
     * 备注
     */
    private String remark;

    /**
     * 审批人姓名
     */
    private String username;

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