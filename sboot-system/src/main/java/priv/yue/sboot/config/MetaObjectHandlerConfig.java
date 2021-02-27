package priv.yue.sboot.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Component;
import priv.yue.sboot.vo.LoginVo;

import java.util.Date;

import static cn.hutool.core.date.DateTime.now;

/**
 * mp 自动填充类
 *
 * @author ZhangLongYue
 * @description
 * @since 2020/12/25 11:48
 */
@Slf4j
@Component
public class MetaObjectHandlerConfig implements MetaObjectHandler {

    private static final String SYSTEM_NAME = "SYSTEM";

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", Date.class, now());
        this.strictInsertFill(metaObject, "createBy", String.class, getUsername());
        this.strictInsertFill(metaObject, "deleted", Integer.class, 0);
        this.strictInsertFill(metaObject, "enabled", Integer.class, 1);
        this.strictInsertFill(metaObject, "sort", Integer.class, 99);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", Date.class, now());
        this.strictUpdateFill(metaObject, "updateBy", String.class, getUsername());
    }

    /**
     * 获取用户名，这里需要注意，如果不是用户请求的操作，如系统调度任务的日志，无法获取到用户信息，
     * 日志由系统创建，没有实际的创建者。
     */
    private String getUsername() {
        LoginVo loginVo;
        try {
            loginVo = (LoginVo) SecurityUtils.getSubject().getPrincipal();
        } catch (Exception e) {
            return SYSTEM_NAME;
        }
        return loginVo.getUser().getUsername();
    }
}
