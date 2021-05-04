package priv.yue.common.base;

import cn.hutool.core.util.StrUtil;
import org.apache.shiro.SecurityUtils;
import priv.yue.common.domain.User;
import priv.yue.common.vo.LoginVo;

/**
 * 类描述信息
 *
 * @author ZhangLongYue
 * @description
 * @since 2020/12/25 10:31
 */
public class BaseController implements GetCurrUser{

    private static final String DEFAULT_ORDER_BY = "create_time desc";

    /**
     * 根据前端返回的排序字段拼接order by
     * 如果是多表联查，例如按用户时间排序，前端返回属性名 => createTime
     * sql中需要加上前缀表名 => user.createTime ，通过 tableAlias 设置前缀
     * <P>
     * 如果前端返回值为 user.createTime 使用该方法
     * {@link BaseController#getSortStr(String[], boolean[])}
     * <P>
     * 注意：暂不支持从表的表别名设置，如果要按从表字段排序，需保证传入的排序字段的前缀与执行sql中的表别名一致
     *
     * @param tableAlias    sql中的表别名，适用于多表联查，尽量使用全表名
     * @param sortBy        需要排序的字段
     * @param sortDesc      是否降序 与sortBy一一对应
     * @return "xxx.xxx desc, xxx.xxx desc"
     */
    public static String getSortStr(String tableAlias, String[] sortBy, boolean[] sortDesc) {
        if (sortBy == null || sortBy.length == 0) return tableAlias + StrUtil.C_DOT + DEFAULT_ORDER_BY;
        if (sortDesc == null || sortDesc.length == 0) return tableAlias + StrUtil.C_DOT + DEFAULT_ORDER_BY;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sortBy.length; i++) {
            if (!sortBy[i].contains(".") && !tableAlias.equals("")) {
                sb.append(tableAlias).append(".");
            }
            sb.append(StrUtil.toUnderlineCase(sortBy[i]));
            if(sortDesc[i]){
                sb.append(" desc");
            }
            sb.append(",");
        }
        return sb.deleteCharAt(sb.length()-1).toString();
    }

    public static String getSortStr(String[] sortBy, boolean[] sortDesc) {
        return getSortStr("", sortBy, sortDesc);
    }

}
