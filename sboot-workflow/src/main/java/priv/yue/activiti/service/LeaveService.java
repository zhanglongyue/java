package priv.yue.activiti.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import priv.yue.activiti.domain.Leave;
import priv.yue.activiti.dto.LeaveDto;
import priv.yue.common.base.BaseService;

import java.util.List;
import java.util.Map;

/**
 * @author ZhangLongYue
 * @since 2021-05-02 15:13:41
 */

public interface LeaveService extends BaseService<Leave> {

    Page<Leave> selectPage(Page<Leave> page, Map<String, Object> map);

}
