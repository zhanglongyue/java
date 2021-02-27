package priv.yue.sboot.utils;

import org.apache.shiro.SecurityUtils;
import priv.yue.sboot.domain.Dept;
import priv.yue.sboot.domain.Role;
import priv.yue.sboot.domain.User;
import priv.yue.sboot.vo.LoginVo;

import java.util.List;

/**
 * 认证工具类
 *
 * @author ZhangLongYue
 * @description
 * @since 2020/12/25 10:54
 */
public class AuthUtils {

    public static LoginVo getLoginInfo(){
        return (LoginVo) SecurityUtils.getSubject().getPrincipal();
    }

    public static User getLoginUser(){
        return getLoginInfo().getUser();
    }

    public static Long getLoginUserId(){
        return getLoginInfo().getUser().getUserId();
    }

    public static String getLoginUserName(){
        return getLoginInfo().getUser().getUsername();
    }

    public static List<Role> getLoginUserRoles(){
        return getLoginInfo().getRoles();
    }

    public static Dept getLoginUserDept(){
        return getLoginInfo().getUser().getDept();
    }

}
