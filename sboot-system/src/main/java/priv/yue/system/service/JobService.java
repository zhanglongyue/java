package priv.yue.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import priv.yue.common.base.BaseService;
import priv.yue.common.domain.Job;

import java.util.Map;

/**
 * @author ZhangLongYue
 * @since 2021/5/8 16:42
 */
public interface JobService extends BaseService<Job> {

    Page<Job> selectPage(Page<Job> page, Map<String, Object> map);

}
