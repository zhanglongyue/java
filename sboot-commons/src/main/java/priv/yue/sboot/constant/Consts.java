package priv.yue.sboot.constant;

/**
 * <p>
 * 常量池
 * </p>
 */
public interface Consts {
    /**
     * 启用
     */
    Integer ENABLE = 1;
    /**
     * 禁用
     */
    Integer DISABLE = 0;

    /**
     * 页面
     */
    Integer PAGE = 1;

    /**
     * 按钮
     */
    Integer BUTTON = 2;

    /**
     * JWT 在 Redis 中保存的key前缀
     */
    String REDIS_JWT_KEY_PREFIX = "security:jwt:";

    /**
     * 星号
     */
    String SYMBOL_STAR = "*";

    /**
     * 邮箱符号
     */
    String SYMBOL_EMAIL = "@";

    /**
     * 默认当前页码
     */
    Integer DEFAULT_CURRENT_PAGE = 1;

    /**
     * 默认每页条数
     */
    Integer DEFAULT_PAGE_SIZE = 10;

    /**
     * 匿名用户 用户名
     */
    String ANONYMOUS_NAME = "匿名用户";

    /**
     * token信息前缀, key:token,value:user_info
     */
    String SHIRO_TOKEN_PREFIX = "SHIRO:TOKEN:";

    /**
     * 用户token前缀, key:user_id,value:token
     */
    String SHIRO_USER_PREFIX = "SHIRO:USER:";

    /**
     * 重置密码默认密码
     */
    String DEFAULT_PASSWORD = "888888";
}
