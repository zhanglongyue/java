package priv.yue.common.exception.handler;

import lombok.Data;

/**
 * @author Zheng Jie
 * @date 2018-11-23
 */
@Data
public class ApiError {

    private Integer code = 400;
    /*@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;*/
    private String msg;

    private ApiError() {
        /*timestamp = LocalDateTime.now();*/
    }

    public static ApiError error(String msg){
        ApiError apiError = new ApiError();
        apiError.setMsg(msg);
        return apiError;
    }

    public static ApiError error(Integer code, String msg){
        ApiError apiError = new ApiError();
        apiError.setCode(code);
        apiError.setMsg(msg);
        return apiError;
    }
}


