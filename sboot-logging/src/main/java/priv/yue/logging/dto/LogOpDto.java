package priv.yue.logging.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import priv.yue.common.base.BaseDto;
import priv.yue.common.dto.PageDto;

import javax.validation.Valid;
import java.io.Serializable;

/**
 * User前端传参封装验证
 *
 * @author ZhangLongYue
 * @description
 * @since 2020/12/15 16:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogOpDto extends BaseDto implements Serializable {

    private static final long serialVersionUID = 2885367604900494545L;

    private String businessType;

    private Integer status;

    @Valid
    private PageDto pager;

}
