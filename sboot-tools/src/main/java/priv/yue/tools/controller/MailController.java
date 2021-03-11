package priv.yue.tools.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import priv.yue.tools.dto.MailDto;
import priv.yue.common.model.RestResult;
import priv.yue.common.model.RestResultUtils;
import priv.yue.tools.service.MailService;

/**
 * @author ZhangLongYue
 * @since 2021/3/8 10:12
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/mail")
public class MailController {

    private MailService mailService;

    @PostMapping("/send")
    @RequiresPermissions("mail:send")
    public RestResult<Object> list(@Validated MailDto mailDto) {
        mailService.sendHtmlEmail(mailDto.getContent(), mailDto.getTo(), mailDto.getSubject());
        return RestResultUtils.success();
    }
}
