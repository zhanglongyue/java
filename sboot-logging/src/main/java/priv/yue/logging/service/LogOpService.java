package priv.yue.logging.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import priv.yue.common.base.BaseService;
import priv.yue.logging.domain.LogOp;

import java.util.Map;

/**
 * @author ZhangLongYue
 * @since 2021/2/1 11:32
 */
public interface LogOpService extends BaseService<LogOp> {

    Page<LogOp> selectPage(Page<LogOp> page, Map<String, Object> map);

}
