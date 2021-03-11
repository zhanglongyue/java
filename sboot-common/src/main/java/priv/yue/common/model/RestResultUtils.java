package priv.yue.common.model;

import org.springframework.http.HttpStatus;


/**
 * @author ZhangLongYue
 * @since 2021/3/10 13:33
 */
public class RestResultUtils {

    public static <T> RestResult<T> success() {
        return RestResult.<T>builder().withCode(HttpStatus.OK.value()).build();
    }

    public static <T> RestResult<T> success(T data) {
        return RestResult.<T>builder().withCode(HttpStatus.OK.value()).withData(data).build();
    }

    public static <T> RestResult<T> success(String msg, T data) {
        return RestResult.<T>builder().withCode(HttpStatus.OK.value()).withMsg(msg).withData(data).build();
    }

    public static <T> RestResult<T> success(int code, T data) {
        return RestResult.<T>builder().withCode(code).withData(data).build();
    }

    public static <T> RestResult<T> failed() {
        return RestResult.<T>builder().withCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).build();
    }

    public static <T> RestResult<T> failed(String errMsg) {
        return RestResult.<T>builder().withCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).withMsg(errMsg).build();
    }

    public static <T> RestResult<T> failed(String errMsg, T data) {
        return RestResult.<T>builder().withCode(HttpStatus.INTERNAL_SERVER_ERROR.value()).withMsg(errMsg).withData(data).build();
    }

    public static <T> RestResult<T> failed(int code, T data) {
        return RestResult.<T>builder().withCode(code).withData(data).build();
    }

    public static <T> RestResult<T> failed(int code, T data, String errMsg) {
        return RestResult.<T>builder().withCode(code).withData(data).withMsg(errMsg).build();
    }

    public static <T> RestResult<T> failedWithMsg(int code, String errMsg) {
        return RestResult.<T>builder().withCode(code).withMsg(errMsg).build();
    }

    public static <T> RestResult<T> buildResult(IResultCode resultCode, T data) {
        return RestResult.<T>builder().withCode(resultCode.getCode()).withMsg(resultCode.getCodeMsg()).withData(data).build();
    }
}
