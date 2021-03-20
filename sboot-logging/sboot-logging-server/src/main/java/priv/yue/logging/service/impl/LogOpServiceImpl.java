package priv.yue.logging.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import priv.yue.common.base.BaseServiceImpl;
import priv.yue.logging.domain.LogOp;
import priv.yue.logging.mapper.LogOpMapper;
import priv.yue.logging.service.LogOpService;

import java.util.Map;

/**
 * @author ZhangLongYue
 * @since 2021/2/1 11:34
 */
@Slf4j
@AllArgsConstructor
@Service
@Transactional
public class LogOpServiceImpl extends BaseServiceImpl<LogOpMapper, LogOp> implements LogOpService/*, LogOpCompletionHandler*/ {

    private LogOpMapper logOpMapper;

    public int insert(LogOp logOp) {
        return logOpMapper.insert(logOp);
    }

    public Page<LogOp> selectPage(Page<LogOp> page, Map<String, Object> map) {
        return logOpMapper.selectPage(page, map);
    }
}
