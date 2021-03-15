package priv.yue.system.handler.fallback;

import priv.yue.common.model.RestResult;
import priv.yue.common.model.RestResultUtils;
import priv.yue.system.dto.UserDto;

/**
 * @author ZhangLongYue
 * @since 2021/3/15 11:27
 */
public class UserFallbackHandler {

    /**
     * 必须是static修饰，方法参数必须与原方法匹配，并增加额外参数Throwable
     * https://github.com/alibaba/Sentinel/wiki/%E6%B3%A8%E8%A7%A3%E6%94%AF%E6%8C%81
     */
    public static RestResult<Object> fallbackHandler(UserDto userDto , Throwable e){
        return RestResultUtils.failed(500, "Fallback", e.getMessage());
    }

}
