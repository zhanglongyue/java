package priv.yue.common.model;

/**
 * IResultCode.
 *
 * @author klw
 * @ClassName: IResultCode
 * @Description: result code enum needs to be implemented this interface
 * @date 2019/6/28 14:44
 */
public interface IResultCode {

    /**
     * Get the result code.
     *
     * @return code value.
     */
    int getCode();

    /**
     * Get the result code's message.
     *
     * @return code's message.
     */
    String getCodeMsg();
}
