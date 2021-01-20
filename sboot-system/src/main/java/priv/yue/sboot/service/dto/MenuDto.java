package priv.yue.sboot.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Menu前端传参封装验证
 *
 * @author ZhangLongYue
 * @since 2020-11-17 15:17:03
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuDto extends BaseDto implements Serializable {

    private static final long serialVersionUID = -4573123415728558990L;
    /**
     * ID
     */
    @Null(groups = Create.class, message = "id必须为空")
    @NotNull(groups = Update.class, message = "id不能为空")
	private Long menuId;
    /**
     * 上级菜单ID
     */
    @NotNull(groups = Create.class, message = "上级id不能为空")
    private Long pid;
    /**
     * 菜单类型
     */
    private Integer type;
    /**
     * 菜单标题
     */
    @NotBlank(groups = Create.class, message = "标题不能为空")
    @Size(groups = {Create.class, Update.class }, min = 1, max = 20, message = "长度必须在{min}-{max}之间")
    private String title;
    /**
     * 组件名称
     */
    private String name;
    /**
     * 组件
     */
    private String component;
    /**
     * 排序
     */
    @Range(min = 1, max = 999, message = "必须在{min}-{max}之间")
    private Integer sort;
    /**
     * 图标
     */
    private String icon;
    /**
     * 链接地址
     */
    private String path;
    /**
     * 是否外链
     */
    private Integer iframe;
    /**
     * 缓存
     */
    @Range(min = 0, max = 1, message = "只能为0或1")
    private Integer cache;
    /**
     * 隐藏
     */
    @Range(min = 0, max = 1, message = "只能为0或1")
    private Integer hidden;
    /**
     * 权限
     */
    private String permission;

}