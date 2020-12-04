package priv.yue.sboot.common;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * REST接口统一返回数据工具类封装RestResponse
 * @param <T>
 */
public class RestResponse<T> implements Serializable {

    private static final long serialVersionUID = 3728877563912075885L;

    private int code;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public RestResponse(){

    }

    public RestResponse(int code, String message, T data) {
        this.code = code;
        this.setMessage(message);
        this.data = data;
    }

    public RestResponse(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public RestResponse(int code, String message) {
        this.code = code;
        this.setMessage(message);
    }

    public static <T> RestResponse<T> success() { return new RestResponse<T>(200, "success"); }

    /**
     * 成功时-返回data
     * @param <T>
     * @return
     */
    public static <T> RestResponse<T> success(T data){
        return new RestResponse<T>(200, "success", data);
    }

    /**
     * 成功-不返回data
     * @param <T>
     * @return
     */
    public static <T> RestResponse<T> success(String message){
        return new RestResponse<T>(200, message);
    }

    /**
     * 成功-返回data+msg
     * @param <T>
     * @return
     */
    public static <T> RestResponse<T> success(String message, T data){
        return new RestResponse<T>(200, message, data);
    }

    /**
     * 失败
     * @param <T>
     * @return
     */
    public static <T> RestResponse<T> fail(String message){
        return new RestResponse<T>(500, message,null);
    }

    /**
     * 失败-code
     * @param <T>
     * @return
     */
    public static <T> RestResponse<T> fail(int code, String message){
        return new RestResponse<T>(code, message,null);
    }


    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }


    public T getData() {
        return data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RestResponse{" + "code=" + code + ", message='" + message + '\'' +", data=" + data +'}';
    }
}
