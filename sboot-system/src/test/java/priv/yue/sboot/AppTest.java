package priv.yue.sboot;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import priv.yue.sboot.domain.User;
import priv.yue.sboot.service.UserService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
class AppTest {

    @Resource
    private UserService userService;

    @Test
    void userList() {
        long ms = System.currentTimeMillis();
        System.out.println(ms);
        Map<String,Object> map = new HashMap<>();
        map.put("search", "");
        map.put("orderBy", "create_time desc");
        map.put("path", ",1,");
        map.put("userDeptId", "1");
        Page<User> page = new Page<>(1, 10);
        page = userService.selectPage(page, map);
        System.out.println(System.currentTimeMillis()-ms);
    }

}
