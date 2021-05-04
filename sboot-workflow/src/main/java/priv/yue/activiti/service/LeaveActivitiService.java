package priv.yue.activiti.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import priv.yue.activiti.domain.Leave;
import priv.yue.activiti.dto.LeaveDto;
import priv.yue.common.base.BaseService;

import java.util.Map;

/**
 * @author ZhangLongYue
 * @since 2021-05-02 15:13:41
 */

public interface LeaveActivitiService extends BaseService<Leave> {

    Leave save(LeaveDto leaveDto);

    Leave update(LeaveDto leaveDto);

}
