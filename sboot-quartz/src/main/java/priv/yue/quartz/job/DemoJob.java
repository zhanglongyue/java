package priv.yue.quartz.job;

import org.springframework.stereotype.Component;

/**
 * @author ZhangLongYue
 * @since 2021/2/19 16:42
 */
@Component
public class DemoJob {

    protected void run(String params){
        System.out.println("测试任务参数：" + params);
        System.out.println(String.format("now2 is %d", System.currentTimeMillis()));
    }

    protected void run(){
        System.out.println(String.format("now2 is %d", System.currentTimeMillis()));
    }

    protected void runException(String params){
        System.out.println("测试任务参数：" + params);
        Integer a = 10/0;
        System.out.println(String.format("now2 is %d", System.currentTimeMillis()));
    }

    protected void runException(){
        Integer a = 10/0;
        System.out.println(String.format("now2 is %d", System.currentTimeMillis()));
    }



}