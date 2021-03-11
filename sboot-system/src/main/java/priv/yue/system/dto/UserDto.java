package priv.yue.system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;
import priv.yue.common.base.BaseDto;
import priv.yue.common.dto.PageDto;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;

/**
 * User前端传参封装验证
 *
 * @author ZhangLongYue
 * @description
 * @since 2020/12/15 16:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto extends BaseDto implements Serializable {

    private static final long serialVersionUID = 2885367604900494545L;

    /**
     * ID
     */
    @Null(groups = Create.class, message = "id必须为空")
    @NotNull(groups = Update.class, message = "id不能为空")
    private Long userId;
    /**
     * 部门名称
     */
    @NotNull(groups = Create.class, message = "部门不能为空")
    private Long deptId;
    /**
     * 用户名
     */
    @NotBlank(groups = Create.class, message = "用户名不能为空")
    @Size(min = 2, max = 20, message="用户名长度必须在{min}-{max}之间")
    private String username;
    /**
     * 昵称
     */
    @Size(min = 2, max = 30, message="昵称长度必须在{min}-{max}之间")
    private String nickName;
    /**
     * 性别
     */
    private String gender;
    /**
     * 手机号码
     */
    @NotBlank(groups = Create.class, message = "手机号不能为空")
    @Pattern(regexp = "^((13[0-9])|(15[^4,\\D])|(18[^1^4,\\D]))\\d{8}", message = "手机号格式不正确")
    private String phone;
    /**
     * 邮箱
     */
    @NotBlank(groups = Create.class, message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;
    /**
     * 头像地址
     */
    private String avatarName;
    /**
     * 头像真实路径
     */
    private String avatarPath;
    /**
     * 密码
     */
    @NotBlank(groups = Create.class, message = "密码不能为空")
    private String password;
    /**
     * 是否为admin账号
     */
    @Range(groups = Create.class, min = 0, max = 1, message = "只能为0或1")
    private Integer isAdmin;
    /**
     * 状态：1启用、0禁用
     */
    @Range(min = 0, max = 1, message = "只能为0或1")
    private Integer enabled;

    @NotEmpty(groups = Create.class, message = "角色不能空")
    private List<RoleDto> roles;

    @Valid
    private PageDto pager;

}
