package priv.yue.sboot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import priv.yue.sboot.domain.Menu;
import priv.yue.sboot.mapper.MenuMapper;
import priv.yue.sboot.service.MenuService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统菜单服务接口实现
 *
 * @author zly
 * @since 2020-11-17 15:17:03
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@AllArgsConstructor
@Service
@Transactional
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

}