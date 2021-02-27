package priv.yue.sboot.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 验证信息封装类
 *
 * @author ZhangLongYue
 * @description
 * @since 2020/12/17 9:39
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ValidateVo {

    /**
     * 验证错误的参数名称
     */
    private String field;

    /**
     * 验证错误信息
     */
    private String Message;

}
