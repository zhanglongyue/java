package priv.yue.system.handler;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import priv.yue.common.model.RestResult;
import priv.yue.common.model.RestResultUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义BlockException的返回值
 * @author ZhangLongYue
 * @since 2021/3/15 11:17
 */
@Component
public class GlobalBlockHandler implements BlockExceptionHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlockException e) throws Exception {

        RestResult<Object> restResult = new RestResult<>();

        if (e instanceof FlowException) {
            restResult = RestResultUtils.failed("服务限流", e.getMessage());
        } else if (e instanceof DegradeException) {
            restResult = RestResultUtils.failed("服务降级", e.getMessage());
        } else if (e instanceof ParamFlowException) {
            restResult = RestResultUtils.failed("热点限流", e.getMessage());
        } else if (e instanceof SystemBlockException) {
            restResult = RestResultUtils.failed("系统规则限制", e.getMessage());
        } else if (e instanceof AuthorityException) {
            restResult = RestResultUtils.failed("授权不通过", e.getMessage());
        }

        // http状态码
        httpServletResponse.setStatus(500);
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setHeader("Content-Type", "application/json;charset=utf-8");
        httpServletResponse.setContentType("application/json;charset=utf-8");

        // spring mvc自带的json操作工具jackson
        new ObjectMapper()
                .writeValue(
                        httpServletResponse.getWriter(),
                        restResult
                );
    }

    @PostConstruct
    public void init() {
        new GlobalBlockHandler();
    }

}
