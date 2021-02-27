package priv.yue.sboot.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ZhangLongYue
 * @since 2021/2/20 15:12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("quartz_job")
public class QuartzJob extends Model<QuartzJob> implements Serializable {

    @TableField(exist = false)
    public static final String JOB_KEY = "JOB_KEY";
    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long jobId;

    /**
     * 部门id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long deptId;

    /**
     * Spring Bean名称
     */
    private String beanName;

    /**
     * cron 表达式
     */
    private String cronExpression;

    /**
     * 状态：1启用、0停用
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer enabled;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 参数
     */
    private String params;

    /**
     * 备注
     */
    private String description;

    /**
     * 负责人
     */
    private String personInCharge;

    /**
     * 报警邮箱
     */
    private String email;

    /**
     * 任务失败后是否暂停
     */
    private Boolean pauseAfterFailure;

    /**
     * 最后一次执行状态 -1初始化 1成功 0失败
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer lastExecIsSuccess;

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
}
