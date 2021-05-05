package priv.yue.activiti.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import priv.yue.common.base.BaseDto;
import priv.yue.common.dto.PageDto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.Date;

/**
 * @author ZhangLongYue
 * @since 2021/5/2 9:10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveDto extends BaseDto implements Serializable{

    /**
     * ID
     */
    @Null(groups = Create.class, message = "id必须为空")
    @NotNull(groups = Update.class, message = "id不能为空")
    private Long id;
    /**
     * 部门名称
     */
    private Long deptId;
    /**
     * 请假原因
     */
    @NotBlank(groups = Create.class, message = "名称不能为空")
    private String reason;
    /**
     * 请假开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    /**
     * 请假结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    /**
     * 请假天数
     */
    private Integer days;
    /**
     * 请假小时数
     */
    private Integer hours;

    @Valid
    private PageDto pager;
}
