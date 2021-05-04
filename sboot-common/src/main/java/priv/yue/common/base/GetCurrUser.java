package priv.yue.common.base;

import org.apache.shiro.SecurityUtils;
import priv.yue.common.domain.User;
import priv.yue.common.vo.LoginVo;

/**
 * @author ZhangLongYue
 * @since 2021/5/3 11:04
 */
public interface GetCurrUser {

    default User getUser(){
        return getLoginVo().getUser();
    }

    default LoginVo getLoginVo(){
        return (LoginVo) SecurityUtils.getSubject().getPrincipal();
    }

}
