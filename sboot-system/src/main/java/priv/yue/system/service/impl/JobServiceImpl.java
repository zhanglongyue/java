package priv.yue.system.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import priv.yue.common.base.BaseServiceImpl;
import priv.yue.common.domain.Job;
import priv.yue.system.domain.maps.JobMap;
import priv.yue.system.mapper.JobMapper;
import priv.yue.system.service.JobService;

import java.util.Map;

/**
 * @author ZhangLongYue
 * @since 2021/5/8 16:42
 */
@Slf4j
@AllArgsConstructor
@Service
@Transactional
public class JobServiceImpl  extends BaseServiceImpl<JobMapper, Job> implements JobService {

    private JobMapper jobMapper;
    private JobMap jobMap;

    @Override
    public Page<Job> selectPage(Page<Job> page, Map<String, Object> map) {
        return jobMapper.selectPage(page, map);
    }
}
