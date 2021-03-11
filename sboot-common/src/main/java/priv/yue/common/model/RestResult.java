package priv.yue.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

public class RestResult<T> implements Serializable {

    private static final long serialVersionUID = 3728877563912075885L;

    private int code;

    private String msg;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public RestResult(){
    }

    public RestResult(int code, String msg, T data) {
        this.code = code;
        this.setMsg(msg);
        this.data = data;
    }

    public RestResult(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public RestResult(int code, String msg) {
        this.code = code;
        this.setMsg(msg);
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

    @Override
    public String toString() {
        return "RestResult{" + "code=" + code + ", msg='" + msg + '\'' + ", data=" + data + '}';
    }

    public static <T> RestResultBuilder<T> builder() {
        return new RestResultBuilder<T>();
    }

    public static final class RestResultBuilder<T> {

        private int code;

        private String errMsg;

        private T data;

        private RestResultBuilder() {
        }

        public RestResultBuilder<T> withCode(int code) {
            this.code = code;
            return this;
        }

        public RestResultBuilder<T> withMsg(String errMsg) {
            this.errMsg = errMsg;
            return this;
        }

        public RestResultBuilder<T> withData(T data) {
            this.data = data;
            return this;
        }

        /**
         * Build result.
         *
         * @return result
         */
        public RestResult<T> build() {
            RestResult<T> restResult = new RestResult<T>();
            restResult.setCode(code);
            restResult.setMsg(errMsg);
            restResult.setData(data);
            return restResult;
        }
    }

}
