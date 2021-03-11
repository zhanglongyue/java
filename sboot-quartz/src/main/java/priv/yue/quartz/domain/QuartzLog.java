package priv.yue.quartz.domain;

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
 * @since 2021/2/24 11:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("quartz_log")
public class QuartzLog extends Model<QuartzLog> implements Serializable {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long logId;

    /**
     * 任务ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long jobId;

    /**
     * Spring Bean名称
     */
    private String beanName;

    /**
     * cron 表达式
     */
    private String cronExpression;

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
     * 执行时长
     */
    private Long time;

    /**
     * 执行结果
     */
    private Boolean isSuccess;

    /**
     * 异常内容
     */
    private String exceptionDetail;

    /**
     * 创建日期
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

}
