package priv.yue.sboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import priv.yue.sboot.base.BaseDto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author ZhangLongYue
 * @since 2021/1/30 13:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDto implements Serializable {

    private static final long serialVersionUID = 569557418505990600L;

    @NotNull(groups = BaseDto.Query.class, message = "page不能为空")
    private Integer page;

    @NotNull(groups = BaseDto.Query.class, message = "itemsPerPage不能为空")
    private Integer itemsPerPage;   // pageSize

    private String [] sortBy;

    private boolean [] sortDesc;

    private String [] groupBy;

    private String [] groupDesc;
}
