package com.zyy.controller;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.zyy.entity.Users;
import com.zyy.service.impl.MailServiceImpl;
import com.zyy.service.impl.UserServiceImpl;
import com.zyy.util.AuthCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Random;

@Slf4j
@RestController
@RequestMapping("/zyy/user")
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private MailServiceImpl mailService;

    @RequestMapping("/register")
    public String Register(@RequestBody String body,HttpServletRequest request){
        Map<String,Object> map=JSON.parseObject(body,Map.class);
        String email= String.valueOf(map.get("email"));
        String userId=userService.SelectIdByEmail(email);
        if (userId!=null){
            return "邮箱已注册";
        }
        Users user=new Users();
        Random random=new Random();
        String id=String.valueOf(random.nextInt(99999)+300000);
        String account=String.valueOf(map.get("account"));
        String password=String.valueOf(map.get("password"));
        user.setId(id);
        user.setAccount(account);
        user.setPassword(password);
        user.setEmail(email);
        int result=userService.Regist(user);
        if(result==1){
            return "注册成功";
        }else {
            return "注册失败";
        }
    }

    @PostMapping("/getEmailCode")
    public String GetCode(@RequestBody String email, HttpServletRequest request, HttpServletResponse response){
        Map<String,Object> map= JSON.parseObject(email,Map.class);
        String mail= (String) map.get("email");
        String code= AuthCodeUtil.getUUID();
        String key=AuthCodeUtil.getUUID();
        response.setHeader("emailSession",key);
        response.setHeader("Access-Control-Expose-Headers","emailSession");
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
                "        <p>您本次的验证码为: "+code+"<span>,3分钟内有效。</span></p>\n" +
                "        <p>请勿泄露和转发。如非本人操作，请忽略此邮件。</p>\n" +
                "    </div>\n" +
                "    <div id=\"sign\" style=\"font-size: 13px\">大学生智能兼职管理平台</div>\n" +
                "</body>\n" +
                "</html>\n";
        mailService.sendWithHtml(mail,subject,content);
        return "发送成功";
    }


}
