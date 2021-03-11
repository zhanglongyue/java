package priv.yue.tools.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import priv.yue.common.base.BaseDto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author ZhangLongYue
 * @since 2021/3/9 10:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailDto extends BaseDto implements Serializable {

    private static final long serialVersionUID = 2831925794893436019L;

    @NotNull(message = "主题不能为空")
    private String subject;

    @NotNull(message = "内容不能为空")
    private String content;

    @NotNull(message = "收件人不能为空")
    private String[] to;
}
