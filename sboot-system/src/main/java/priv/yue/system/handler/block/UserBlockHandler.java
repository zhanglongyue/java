package priv.yue.system.handler.block;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import priv.yue.common.model.RestResult;
import priv.yue.common.model.RestResultUtils;
import priv.yue.system.dto.UserDto;

/**
 * sentinel配置限流降级可以按url（簇点链路配置），也可以按@SentinelResource中的资源名配置
 * 自定义的blockHandler方法要生效必须使用资源名的方式进行配置
 * 使用url配置将使用sentinel默认的blockHandler方法，即返回Blocked By Sentinel
 *
 * @author ZhangLongYue
 * @since 2021/3/15 11:27
 */
public class UserBlockHandler {

    /**
     * 必须是static修饰，方法参数必须与原方法匹配，并增加额外参数BlockException
     * https://github.com/alibaba/Sentinel/wiki/%E6%B3%A8%E8%A7%A3%E6%94%AF%E6%8C%81
     */
    public static RestResult<Object> blockHandler(UserDto userDto, BlockException e){
        return RestResultUtils.failed(500, "Block", e.getMessage());
    }

}
