package priv.yue.logging.core;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.novelweb.tool.annotation.log.OpLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import priv.yue.common.domain.User;
import priv.yue.common.utils.JsonUtils;
import priv.yue.common.vo.LoginVo;
import priv.yue.logging.model.LogOp;
import priv.yue.sboot.stream.IMessageProvider;

import javax.annotation.Resource;

/**
 * 操作日志的注解实现类,通过Stream发送消息,由日志消息订阅方完成日志记录
 * {@link priv.yue.logging.mq.listener.LogListener#asyncLog}
 *
 * @author ZhangLongYue
 * @since 2021-02-02 11:06:03
 */
@Aspect
@Component
@Slf4j
public class LogAspect {

    @Resource
    private IMessageProvider messageProvider;

    /**
     * 配置切入点
     */
    @Pointcut("@annotation(cn.novelweb.tool.annotation.log.OpLog)")
    public void opLog() {}

    private void handleLog(final JoinPoint joinPoint, final Throwable e,
                           Object res, long time) {
        // 获得注解信息
        OpLog opLog = LogAnnotation.getAnnotation(joinPoint, OpLog.class);
        if (opLog == null) { return; }

        // 初始化日志信息
        LogOp logOp = Convert.convert(LogOp.class, LogAnnotation.initInfo(opLog.title(), opLog.isGetIp(), e));

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
            log.error(ExceptionUtil.getMessage(exception));
        }

        logOp.setReturnValue(JsonUtils.toJsonWithSingleQuotes(res));

        // 是否需要保存URL的请求参数
        if (opLog.isSaveRequestData()) {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes)
                    RequestContextHolder.getRequestAttributes();
            if (requestAttributes != null) {
                logOp.setParameter(JsonUtils.toJsonWithSingleQuotes(
                        requestAttributes.getRequest().getParameterMap()));
            } else {
                logOp.setParameter("无法获取request信息");
            }
        }

        // 采用线程方式异步执行任务回调，TaskCallback.callback将会反射调用complete方法
        // ThreadUtil.execAsync(() -> TaskCallback.callback(LogOpServiceImpl.class, logOp));

        // 采用rabbitTemplate方式
        // rabbitMQSender.sendObject(Constants.EXCHANGE, "log.op", JsonUtils.toJson(logOp));

        // Stream方式
        messageProvider.send(logOp);

    }

    @Around("opLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object res = null;
        Exception exception = null;
        long time = System.currentTimeMillis();
        try {
            res =  joinPoint.proceed();
        } catch (Exception e) {
            log.error(ExceptionUtil.getMessage(e));
            // 获取异常信息后抛出，只是为了记录，不改变原有异常处理逻辑
            exception = e;
            throw e;
        } finally {
            time = System.currentTimeMillis() - time;
            handleLog(joinPoint, exception, res, time);
        }
        return res;
    }
}
