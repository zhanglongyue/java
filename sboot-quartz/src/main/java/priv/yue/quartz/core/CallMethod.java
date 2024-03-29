package priv.yue.quartz.core;

import cn.hutool.core.util.StrUtil;
import org.springframework.util.ReflectionUtils;
import priv.yue.common.utils.SpringBeanFactoryUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author ZhangLongYue
 * @since 2021/2/24 11:06
 */
public class CallMethod {

    private Object target;
    private Method method;
    private String params;

    public CallMethod(String beanName, String methodName, String params) throws NoSuchMethodException {
        this.target = SpringBeanFactoryUtils.getBean(beanName);
        this.params = params;

        if (StrUtil.isNotBlank(params)) {
            this.method = target.getClass().getDeclaredMethod(methodName, String.class);
        } else {
            this.method = target.getClass().getDeclaredMethod(methodName);
        }
    }

    public void call() throws InvocationTargetException, IllegalAccessException {
        ReflectionUtils.makeAccessible(method);
        if (StrUtil.isNotBlank(params)) {
            method.invoke(target, params);
        } else {
            method.invoke(target);
        }
    }
}
