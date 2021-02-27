package priv.yue.sboot.log;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.thread.ThreadUtil;
import cn.novelweb.tool.annotation.TaskCallback;
import cn.novelweb.tool.annotation.log.OpLog;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import priv.yue.sboot.domain.LogOp;
import priv.yue.sboot.domain.User;
import priv.yue.sboot.vo.LoginVo;

/**
 * 操作日志的注解实现类
 *
 * @author ZhangLongYue
 * @since 2021-02-02 11:06:03
 */
@Aspect
@Component
public class LogAspect {

    /**
     * 配置切入点
     */
    @Pointcut("@annotation(cn.novelweb.tool.annotation.log.OpLog)")
    public void opLog() {
    }

    private void handleLog(final JoinPoint joinPoint, final Exception e,
                           Object res, long time) {
        // 获得注解信息
        OpLog opLog = LogAnnotation.getAnnotation(joinPoint, OpLog.class);
        if (opLog == null) {
            return;
        }

        // 初始化日志信息
        LogOp logOp = Convert.convert(LogOp.class, LogAnnotation.initInfo(opLog.title(),
                opLog.isGetIp(), e));
        // 设置业务类型、类名、方法名等
        logOp.setBusinessType(opLog.businessType());
        logOp.setClassName(joinPoint.getTarget().getClass().getName());
        logOp.setMethodName(joinPoint.getSignature().getName());
        logOp.setRunTime(time);

        try {
            User user = ((LoginVo) SecurityUtils.getSubject().getPrincipal()).getUser();
            logOp.setUserId(user.getUserId());
            logOp.setDeptId(user.getDeptId());
            logOp.setUsername(user.getUsername());
        } catch (Exception exception) {
        }

        logOp.setReturnValue(JSON.toJSONString(res));

        // 是否需要保存URL的请求参数
        if (opLog.isSaveRequestData()) {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes)
                    RequestContextHolder.getRequestAttributes();
            if (requestAttributes != null) {
                logOp.setParameter(JSONObject.toJSONString(
                        requestAttributes.getRequest().getParameterMap()));
            } else {
                logOp.setParameter("无法获取request信息");
            }
        }
        // 异步执行任务回调
        ThreadUtil.execAsync(() -> TaskCallback
                .callback(LogOpCompletionHandler.class, logOp));
    }

    @Around("opLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object res = null;
        Exception exception = null;
        long time = System.currentTimeMillis();
        try {
            res =  joinPoint.proceed();
            return res;
        } catch (Exception e) {
            exception = e;
            throw e;
        } finally {
            time = System.currentTimeMillis() - time;
            handleLog(joinPoint, exception, res, time);
        }
    }
}
