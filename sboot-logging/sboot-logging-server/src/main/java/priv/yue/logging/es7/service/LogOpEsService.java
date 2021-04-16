package priv.yue.logging.es7.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import priv.yue.logging.es7.domain.LogOpEs;

import java.util.Map;


/**
 * @author ZhangLongYue
 * @since 2021/4/8 14:05
 */
public interface LogOpEsService {

    Page<LogOpEs> selectPage(Map<String, Object> map, Integer page, Integer size);

}
