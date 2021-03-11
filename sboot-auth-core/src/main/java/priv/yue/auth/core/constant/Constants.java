package priv.yue.auth.core.constant;

/**
 * @author ZhangLongYue
 * @since 2021/3/11 10:02
 */
public interface Constants {

    /**
     * 认证请求头
     */
    String AUTHORIZATION = "Authorization";

    /**
     * token信息前缀, key:token,value:user_info
     */
    String SHIRO_TOKEN_PREFIX = "SHIRO:TOKEN:";

    /**
     * 用户token前缀, key:user_id,value:token
     */
    String SHIRO_USER_PREFIX = "SHIRO:USER:";

    /**
     * 默认token超时时间 30分钟
     */
    Long DEFAULT_TOKEN_TIMEOUT = 1000L * 60 * 30;

    /**
     * 默认记住我时间 7天
     */
    Long REMEBER_ME_TIMEOUT = 1000L * 60 * 60 * 24 * 7;

}
