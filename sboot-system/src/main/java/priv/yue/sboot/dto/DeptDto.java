package priv.yue.sboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import priv.yue.sboot.base.BaseDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Dept前端传参封装验证
 *
 * @author ZhangLongYue
 * @since 2020-11-17 15:13:41
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeptDto extends BaseDto implements Serializable {

    private static final long serialVersionUID = 3849755194017735379L;
    /**
     * ID
     */
    @Null(groups = Create.class, message = "id必须为空")
    @NotNull(groups = Update.class, message = "id不能为空")
	private Long deptId;
    /**
     * 上级部门
     */
    private Long pid;
    /**
     * 名称
     */
    @NotBlank(groups = Create.class, message = "名称不能为空")
    @Size(groups = {Create.class, Update.class }, min = 1, max = 20, message = "长度必须在{min}-{max}之间")
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

}