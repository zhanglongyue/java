package priv.yue.activiti.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import priv.yue.activiti.domain.LeaveFlow;
import priv.yue.activiti.mapper.LeaveFlowMapper;
import priv.yue.activiti.service.LeaveFlowService;
import priv.yue.common.base.BaseServiceImpl;

/**
 * @author ZhangLongYue
 * @since 2021-05-02 15:13:41
 */

@Slf4j
@AllArgsConstructor
@Service
@Transactional
public class LeaveFlowServiceImpl extends BaseServiceImpl<LeaveFlowMapper, LeaveFlow> implements LeaveFlowService{

}




