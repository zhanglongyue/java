package priv.yue.system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import priv.yue.common.base.BaseDto;
import priv.yue.common.dto.PageDto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;

/**
 * Job前端传参封装验证
 *
 * @author ZhangLongYue
 * @description
 * @since 2020/12/15 16:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobDto extends BaseDto implements Serializable {

    private static final long serialVersionUID = 2931340157290757281L;
    /**
     * ID
     */
    @Null(groups = Create.class, message = "id必须为空")
    @NotNull(groups = Update.class, message = "id不能为空")
    private Long jobId;
    /**
     * 部门id
     */
    @NotNull(groups = Create.class, message = "部门不能为空")
    private Long deptId;
    /**
     * 用户名
     */
    @NotBlank(groups = Create.class, message = "名称不能为空")
    private String name;

    /**
     * 排序
     */
    @Range(min = 1, max = 999, message = "必须在{min}-{max}之间")
    private Integer sort;

    /**
     * 状态：1启用、0禁用
     */
    @Range(min = 0, max = 1, message = "只能为0或1")
    private Integer enabled;

    @Valid
    private PageDto pager;

}
