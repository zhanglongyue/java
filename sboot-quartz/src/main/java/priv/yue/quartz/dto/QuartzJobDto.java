package priv.yue.quartz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import priv.yue.common.base.BaseDto;
import priv.yue.common.dto.PageDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;

/**
 * @author ZhangLongYue
 * @since 2021/2/20 15:57
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuartzJobDto extends BaseDto implements Serializable {
    private static final long serialVersionUID = -1116461067710043454L;

    @Null(groups = Create.class, message = "id必须为空")
    @NotNull(groups = Update.class, message = "id不能为空")
    private Long jobId;

    /**
     * 部门id
     */
    private Long deptId;

    /**
     * Spring Bean名称
     */
    @NotNull(groups = Create.class, message = "Bean不能为空")
    private String beanName;

    /**
     * cron 表达式
     */
    @NotNull(groups = Create.class, message = "Cron表达式不能为空")
    private String cronExpression;

    /**
     * 状态：1启用、0停用
     */
    private Integer enabled;

    /**
     * 任务名称
     */
    @NotNull(groups = Create.class, message = "任务名称不能为空")
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
     * 子任务ID
     */
    private String subTask;

    /**
     * 任务失败后是否暂停
     */
    private Boolean pauseAfterFailure;

    @Valid
    private PageDto pager;
}
