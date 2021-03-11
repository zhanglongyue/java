package priv.yue.common.vo;

import lombok.Getter;
import lombok.Setter;
import priv.yue.common.auth.GetAuthorizationAble;
import priv.yue.common.domain.Menu;
import priv.yue.common.domain.Role;
import priv.yue.common.domain.User;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 登录用户信息包装vo
 *
 * @author ZhangLongYue
 * @since 2020-11-17 15:17:03
 * @description
 */
@Getter
@Setter
public class LoginVo implements GetAuthorizationAble {

    private User user;

    private List<Menu> menus;

    private List<Role> roles;

    private Set<String> permissions;

    private Boolean rememberMe;

    public Collection<String> getRolesStr() {
        return getRoles().stream().map(Role::getName).collect(Collectors.toSet());
    }

    public Collection<String> getPermissionsStr() {
        return permissions;
    }
}
