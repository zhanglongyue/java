package priv.yue.system.utils;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponseUtils {

    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private Object data;

    private ResponseUtils() {
        timestamp = LocalDateTime.now();
    }

    public static ResponseUtils ok(String message){
        ResponseUtils responseUtils = new ResponseUtils();
        responseUtils.setMessage(message);
        return responseUtils;
    }

    public static ResponseUtils error(String message){
        ResponseUtils responseUtils = new ResponseUtils();
        responseUtils.setMessage(message);
        return responseUtils;
    }

    public static ResponseUtils error(Integer status, String message){
        ResponseUtils responseUtils = new ResponseUtils();
        responseUtils.setStatus(status);
        responseUtils.setMessage(message);
        return responseUtils;
    }
}

