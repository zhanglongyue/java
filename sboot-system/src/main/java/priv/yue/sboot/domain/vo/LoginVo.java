package priv.yue.sboot.domain.vo;

import lombok.Getter;
import lombok.Setter;
import priv.yue.sboot.domain.Menu;
import priv.yue.sboot.domain.Role;
import priv.yue.sboot.domain.User;

import java.util.List;

@Getter
@Setter
public class LoginVo {

    private String token;

    private User user;

    private List<Menu> menus;

    private List<Role> roles;
}
