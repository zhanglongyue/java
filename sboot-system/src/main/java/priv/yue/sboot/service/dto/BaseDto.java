package priv.yue.sboot.service.dto;

import lombok.Data;

import java.util.Date;

/**
 * dto基类
 *
 * @author ZhangLongYue
 * @description
 * @since 2020/12/22 9:36
 */
@Data
public class BaseDto {
    /**
     *  新增时校验分组
     */
    public interface Create {}
    /**
     *  查询时校验分组
     */
    public interface Query {}
    /**
     *  更新时校验分组
     */
    public interface Update {}
    /**
     *  删除时校验分组
     */
    public interface Delete {}

    /**
     * 大于创建日期
     */
    private String createTimeBegin;
    /**
     * 小于创建日期
     */
    private String createTimeEnd;
    /**
     * 大于更新时间
     */
    private String updateTimeBegin;
    /**
     * 小于更新时间
     */
    private String updateTimeEnd;

    private String search;
}
