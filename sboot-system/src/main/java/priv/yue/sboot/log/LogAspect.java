package priv.yue.sboot.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @author ZhangLongYue
 * @since 2021/1/20 8:46
 */
@Component
@Aspect
@Slf4j
public class LogAspect {
    String methodName;      // 方法名
    long startTime;         // 开始时间

    /**
     * 定义一个切入点.
     * 解释下：
     *
     * ~ 第一个 * 代表任意修饰符及任意返回值.
     * ~ 第二个 * 定义在web包或者子包
     * ~ 第三个 * 任意方法
     * ~ .. 匹配任意数量的参数.
     */
    @Pointcut("execution( public * priv.yue.sboot.service..*.*(..))")
    public void aopPointCut() {
    }

    @Before("aopPointCut()")
    public void doBefore(JoinPoint joinPoint) {
        methodName = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        startTime = System.currentTimeMillis();
    }

    @After("aopPointCut()")
    public void doAfter() {
        long E_time = System.currentTimeMillis() - startTime;
        log.info("执行 " + methodName + " 耗时为：" + E_time + "ms");
    }

    @AfterReturning(returning = "object", pointcut = "aopPointCut()")
    public void doAfterReturning(Object object) {
        log.info("response={}", object.toString());
    }

    @Around("aopPointCut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable{
        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            long end = System.currentTimeMillis();
            log.error("+++++around " + joinPoint + "\tUse time : " + (end - start) + " ms!");
            return result;

        } catch (Throwable e) {
            long end = System.currentTimeMillis();
            log.error("+++++around " + joinPoint + "\tUse time : " + (end - start) + " ms with exception : " + e.getMessage());
            throw e;
        }
    }
}
