package priv.yue.sboot.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import priv.yue.sboot.auth.utils.AuthUtils;

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

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        this.strictInsertFill(metaObject, "createTime", Date.class, now());
        this.strictInsertFill(metaObject, "createBy", String.class, AuthUtils.getLoginUserName());
        this.strictInsertFill(metaObject, "deleted", Integer.class, 0);
        this.strictInsertFill(metaObject, "enabled", Integer.class, 1);
        this.strictInsertFill(metaObject, "pid", Long.class, AuthUtils.getLoginUserDept().getDeptId());
        this.strictInsertFill(metaObject, "sort", Integer.class, 99);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        this.strictUpdateFill(metaObject, "updateTime", Date.class, now());
        this.strictUpdateFill(metaObject, "updateBy", String.class, AuthUtils.getLoginUserName());
    }
}
