package com.zyy.controller;

import com.alibaba.fastjson.JSON;
import com.zyy.service.impl.MailServiceImpl;
import com.zyy.utils.AuthCodeUtils;
import com.zyy.utils.RedisUtils;
import com.zyy.utils.ResponseUtils;
import com.zyy.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.http.HttpResponse;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/zyy/email")
@CrossOrigin(origins = "*")
public class EmailController {

    @Autowired
    private MailServiceImpl mailService;

    @Autowired
    private RedisUtils redisUtil;

    @PostMapping("/getEmailCode")
    public Result GetCode(@RequestBody String body, HttpSession session){
        Map<String,Object> map= JSON.parseObject(body,Map.class);
        String email= (String) map.get("email");
        String code= AuthCodeUtils.getUUID();
        redisUtil.set(email,code);
        session.setAttribute("emailKey",code);
        String subject="大学生智能兼职管理平台";
        String content="<html lang=\"zh\">\n" +
                "<head>\n" +
                "    <meta charset=utf-8>\n" +
                "    <title>ttt</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div id=\"title\">\n" +
                "        <h2 style=\"color: rgb(6, 171, 255)\">邮箱验证码</h2>\n" +
                "    </div>\n" +
                "    <div id=\"context\">\n" +
                "        <p>欢迎使用大学生智能兼职管理平台</p>\n" +
                "        <p>您本次的验证码为: "+code+"<span>,三分钟内有效。</span></p>\n" +
                "        <p>请勿泄露和转发。如非本人操作，请忽略此邮件。</p>\n" +
                "    </div>\n" +
                "    <div id=\"sign\" style=\"font-size: 13px\">大学生智能兼职管理平台</div>\n" +
                "</body>\n" +
                "</html>\n";
        mailService.sendWithHtml(email,subject,content);
        redisUtil.expire(email,60*3);
        return ResponseUtils.successResult("发送成功");
    }
}
