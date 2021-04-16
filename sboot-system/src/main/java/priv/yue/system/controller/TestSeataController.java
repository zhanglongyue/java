package priv.yue.system.controller;

import io.seata.spring.annotation.GlobalTransactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import priv.yue.common.base.BaseController;
import priv.yue.common.domain.User;
import priv.yue.common.model.RestResult;
import priv.yue.common.model.RestResultUtils;
import priv.yue.system.service.TestSeataService;
import priv.yue.system.service.UserService;

/**
 *
 * @author ZhangLongYue
 * @since 2020-11-17 15:17:03
 * @description
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/seata")
public class TestSeataController extends BaseController {

    private UserService userService;

    private TestSeataService testSeataService;

    @GlobalTransactional(timeoutMills = 300000, name = "test-tx")
    @PostMapping("/testSeata")
    public RestResult<Object> seataTest(){
        User user = userService.getById("1355398931975483393");

        // 将用户禁用
        userService.updateById(user.setEnabled(0));

        // 再创建一条定时任务,这条任务会故意创建失败,上面的用户禁用操作会被seata异步回滚
        testSeataService.create();

        return RestResultUtils.success();
    }

}