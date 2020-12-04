package priv.yue.sboot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import priv.yue.sboot.domain.Dept;
import priv.yue.sboot.mapper.DeptMapper;
import priv.yue.sboot.service.DeptService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 部门服务接口实现
 *
 * @author zly
 * @since 2020-11-17 15:13:41
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@AllArgsConstructor
@Service
@Transactional
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {

}