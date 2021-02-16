package priv.yue.sboot.service.impl;

import cn.novelweb.tool.annotation.log.callback.OpLogCompletionHandler;
import cn.novelweb.tool.annotation.log.pojo.OpLogInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import priv.yue.sboot.domain.LogOp;
import priv.yue.sboot.log.LogOpCompletionHandler;
import priv.yue.sboot.mapper.LogOpMapper;
import priv.yue.sboot.service.LogOpService;
import priv.yue.sboot.utils.SpringBeanFactoryUtils;

import java.util.Map;

/**
 * @author ZhangLongYue
 * @since 2021/2/1 11:34
 */
@Slf4j
@AllArgsConstructor
@Service
@Transactional
public class LogOpServiceImpl extends BaseServiceImpl<LogOpMapper, LogOp> implements LogOpService, LogOpCompletionHandler {

    private LogOpMapper logOpMapper;

    public void complete(LogOp opLogInfo) {
        /*System.out.println("操作日志结果:当前访问的ip地址:" + opLogInfo.getIp());
        System.out.println("操作日志结果:ip的实际地理位置:" + opLogInfo.getLocation());
        System.out.println("操作日志结果:浏览器的内核类型:" + opLogInfo.getBrowser());
        System.out.println("操作日志结果:浏览器的内核版本:" + opLogInfo.getBrowserVersion());
        System.out.println("操作日志结果:浏览器解析引擎类型:" + opLogInfo.getBrowserEngine());
        System.out.println("操作日志结果:浏览器解析引擎版本:" + opLogInfo.getBrowserEngineVersion());
        System.out.println("操作日志结果:是否为移动平台访问:" + opLogInfo.getIsMobile());
        System.out.println("操作日志结果:访问的操作系统类型:" + opLogInfo.getOs());
        System.out.println("操作日志结果:访问的操作平台类型:" + opLogInfo.getPlatform());
        System.out.println("操作日志结果:爬虫的类型(如果有):" + opLogInfo.getSpider());
        System.out.println("操作日志结果:除去host部分的路径:" + opLogInfo.getRequestUri());
        System.out.println("操作日志结果:获取出错时异常原因:" + opLogInfo.getErrorCause());
        System.out.println("操作日志结果:获取出错时异常信息:" + opLogInfo.getErrorMsg());
        System.out.println("操作日志结果:自定义访问模块名称:" + opLogInfo.getTitle());
        System.out.println("操作日志结果:访问的状态(0:正常):" + opLogInfo.getStatus());
        System.out.println("操作日志结果:获取到的访问的时间:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(opLogInfo.getCreateTime()));
        // ----------------------------------------- 下面的字段是 AccessLogInfo 没有的 -----------------------------------------
        System.out.println("操作日志结果:自定义访问业务类型:" + opLogInfo.getBusinessType());
        System.out.println("操作日志结果:执行操作的类的名称:" + opLogInfo.getClassName());
        System.out.println("操作日志结果:执行操作的方法名称:" + opLogInfo.getMethodName());
        System.out.println("操作日志结果:获取到访问的url参数:" + opLogInfo.getParameter());*/

        String str = "0:0:0:0:0:0:0:1";
        if (opLogInfo.getIp().equals(str)) {
            opLogInfo.setIp("127.0.0.1");
        }

        // 构建你需要的实体信息 可以增加 或者 去掉某些字段
        // 比如说 增加 当前登录的用户名
        /*LogOp logOp = new LogOp();
        logOp.setIp(opLogInfo.getIp())
             .setLocation(opLogInfo.getLocation())
             .setBrowser(opLogInfo.getBrowser())
             .setBrowserVersion(opLogInfo.getBrowserVersion())
             .setBrowserEngine(opLogInfo.getBrowserEngine())
             .setBrowserEngineVersion(opLogInfo.getBrowserEngineVersion())
             .setIsMobile(opLogInfo.getIsMobile())
             .setOs(opLogInfo.getOs())
             .setPlatform(opLogInfo.getPlatform())
             .setSpider(opLogInfo.getSpider())
             .setRequestUri(opLogInfo.getRequestUri())
             .setErrorCause(opLogInfo.getErrorCause())
             .setErrorMsg(opLogInfo.getErrorMsg())
             .setTitle(opLogInfo.getTitle())
             .setStatus(opLogInfo.getStatus())
             .setCreateTime(opLogInfo.getCreateTime())
             .setBusinessType(opLogInfo.getBusinessType())
             .setClassName(opLogInfo.getClassName())
             .setMethodName(opLogInfo.getMethodName())
             .setParameter(opLogInfo.getParameter());*/

        try {
            // 手动注入服务
            if (logOpMapper == null) {
                logOpMapper = SpringBeanFactoryUtils.getBean(LogOpMapper.class);
            }
            logOpMapper.insert(opLogInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Page<LogOp> selectPage(Page<LogOp> page, Map<String, Object> map) {
        return logOpMapper.selectPage(page, map);
    }
}
