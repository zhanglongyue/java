package priv.yue.sboot.service;

import priv.yue.sboot.base.BaseService;
import priv.yue.sboot.domain.Menu;
import priv.yue.sboot.vo.LoginVo;

/**
 * @author ZhangLongYue
 * @since 2021/2/21 13:18
 */
public interface AuthService extends BaseService<Menu> {
    LoginVo getLoginUserByName(String username);
}
