package priv.yue.sboot.service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import priv.yue.sboot.domain.Menu;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.List;

/**
 * Role前端传参封装验证
 *
 * @author ZhangLongYue
 * @since 2020-11-17 15:07:40
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto extends BaseDto implements Serializable {

    private static final long serialVersionUID = 8397024307507413505L;
    /**
     * ID
     */
    @Null(groups = Create.class, message = "id必须为空")
    @NotNull(groups = Update.class, message = "id不能为空")
	private Long roleId;
    /**
     * 上级角色
     */
    private Long pid;
    /**
     * 名称
     */
    @NotBlank(groups = Create.class, message = "名称不能为空")
    private String name;
    /**
     * 描述
     */
    private String description;
    /**
     * 数据权限
     */
    private String dataScope;
    /**
     * 资源集合
     */
    private List<Menu> menus;
}