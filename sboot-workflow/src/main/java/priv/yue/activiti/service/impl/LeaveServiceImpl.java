package priv.yue.activiti.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import priv.yue.activiti.domain.Leave;
import priv.yue.activiti.domain.maps.LeaveMap;
import priv.yue.activiti.mapper.LeaveMapper;
import priv.yue.activiti.service.LeaveService;
import priv.yue.common.base.BaseServiceImpl;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author ZhangLongYue
 * @since 2021-05-02 15:13:41
 */

@Slf4j
@Service
@Transactional
public class LeaveServiceImpl extends BaseServiceImpl<LeaveMapper, Leave> implements LeaveService {

    @Resource
    protected LeaveMapper leaveMapper;
    @Resource
    protected LeaveMap leaveMap;

    @Override
    public Page<Leave> selectPage(Page<Leave> page, Map<String, Object> map) {
        return leaveMapper.selectPage(page, map);
    }

}




