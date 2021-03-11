package priv.yue.sboot;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;
import priv.yue.common.domain.User;
import priv.yue.system.service.UserService;

import javax.annotation.Resource;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
class SystemMainTest {

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

    @Test
    void export() throws IOException {
        String templateFileName = ResourceUtils.getURL("classpath:").getPath() + "static/excel_templates/user.xlsx";
        try(InputStream is = FileUtil.getInputStream(templateFileName)) {
            try (OutputStream os = new FileOutputStream("excel.xls")) {
                Context context = new Context();
                List<User> list = new ArrayList<>();
                User user = new User();
                user.setUsername("zhangsan");
                user.setNickName("san");
                list.add(user);
                context.putVar("employees", list);
                JxlsHelper.getInstance().processTemplate(is, os, context);
            }
        }
    }

}
