package priv.yue.logging.core;


import priv.yue.logging.model.LogOp;

/**
 * 操作日志处理完成后的回调接口
 *
 * @author ZhangLongYue
 * @since 2021/2/2 11:09
 */
public interface LogOpCompletionHandler {

    /**
     * 操作日志信息处理完成后自动回调该接口
     * 此接口主要用于用户将日志信息存入MySQL、Redis等等
     *
     * @param logOp 处理完成后的 操作日志实体信息
     */
    void complete(LogOp logOp);
}
