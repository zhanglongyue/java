package priv.yue.sboot.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import priv.yue.sboot.base.BaseServiceImpl;
import priv.yue.sboot.domain.QuartzLog;
import priv.yue.sboot.mapper.QuartzLogMapper;
import priv.yue.sboot.service.QuartzLogService;

/**
 * @author ZhangLongYue
 * @since 2021/2/20 15:44
 */
@Slf4j
@AllArgsConstructor
@Service
@Transactional
public class QuartzLogServiceImpl extends BaseServiceImpl<QuartzLogMapper, QuartzLog> implements QuartzLogService {

}
