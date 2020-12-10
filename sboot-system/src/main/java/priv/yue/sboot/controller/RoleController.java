package priv.yue.sboot.controller;


import priv.yue.sboot.service.RoleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 角色表服务控制器
 *
 * @author zly
 * @since 2020-11-17 15:07:40
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/role")
public class RoleController {
    private final RoleService roleService;

}