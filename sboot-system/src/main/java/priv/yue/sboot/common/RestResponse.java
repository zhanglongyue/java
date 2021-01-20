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
    private String msg;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public RestResponse(){

    }

    public RestResponse(int code, String msg, T data) {
        this.code = code;
        this.setMsg(msg);
        this.data = data;
    }

    public RestResponse(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public RestResponse(int code, String msg) {
        this.code = code;
        this.setMsg(msg);
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
    public static <T> RestResponse<T> success(String msg){
        return new RestResponse<T>(200, msg);
    }

    /**
     * 成功-返回data+msg
     * @param <T>
     * @return
     */
    public static <T> RestResponse<T> success(String msg, T data){
        return new RestResponse<T>(200, msg, data);
    }

    /**
     * 失败
     * @param <T>
     * @return
     */
    public static <T> RestResponse<T> fail(String msg){
        return new RestResponse<T>(500, msg,null);
    }

    /**
     * 失败-返回data
     * @param <T>
     * @return
     */
    public static <T> RestResponse<T> fail(String msg, T data){
        return new RestResponse<T>(500, "参数不正确", data);
    }

    /**
     * 失败-code
     * @param <T>
     * @return
     */
    public static <T> RestResponse<T> fail(int code, String msg){
        return new RestResponse<T>(code, msg,null);
    }


    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(T data) {
        this.data = data;
    }

}
