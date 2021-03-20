package priv.yue.auth.service;

import priv.yue.common.base.BaseService;
import priv.yue.common.domain.Menu;
import priv.yue.common.vo.LoginVo;

/**
 * @author ZhangLongYue
 * @since 2021/2/21 13:18
 */
public interface AuthService extends BaseService<Menu> {
    LoginVo getLoginUserByName(String username);
}
