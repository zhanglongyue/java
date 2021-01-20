package priv.yue.sboot.service.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

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
    private Date createTimeBegin;
    /**
     * 小于创建日期
     */
    private Date createTimeEnd;
    /**
     * 大于更新时间
     */
    private Date updateTimeBegin;
    /**
     * 小于更新时间
     */
    private Date updateTimeEnd;

    @NotNull(groups = Query.class, message = "page不能为空")
    private Integer page;

    /**
     * pageSize
     */
    @NotNull(groups = Query.class, message = "itemsPerPage不能为空")
    private Integer itemsPerPage;

    private String [] sortBy;

    private boolean [] sortDesc;

    private String [] groupBy;

    private String [] groupDesc;

    private String search;
}
