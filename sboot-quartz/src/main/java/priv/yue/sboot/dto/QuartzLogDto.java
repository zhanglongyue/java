package priv.yue.sboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import priv.yue.sboot.base.BaseDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author ZhangLongYue
 * @since 2021/2/20 15:57
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuartzLogDto extends BaseDto implements Serializable {
    private static final long serialVersionUID = -1116461067710043454L;

    @NotNull(groups = Query.class, message = "id不能为空")
    private Long jobId;

    @Valid
    private PageDto pager;
}
