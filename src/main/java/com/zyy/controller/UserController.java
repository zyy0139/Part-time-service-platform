package com.zyy.controller;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.util.JsonParserSequence;
import com.zyy.entity.Users;
import com.zyy.service.impl.MailServiceImpl;
import com.zyy.service.impl.UserServiceImpl;
import com.zyy.util.*;
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
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private MailServiceImpl mailService;

    @Autowired
    private RedisUtil redisUtil;

    @PostMapping("/register")
    public Result Register(@RequestBody String body, HttpServletRequest request){
        Map<String,Object> map=JSON.parseObject(body,Map.class);
        String email= String.valueOf(map.get("email"));
        String userId=userService.SelectIdByEmail(email);
        if (userId!=null){
            return ResponseUtil.failResult(ResultCode.USER_EXIST,"该邮箱已注册");
        }
        String emailCode=String.valueOf(map.get("emailCode"));
        String emailKey=request.getHeader("emailSession");
//        String value= null;
//        try {
//            value = redisUtil.get(emailKey).toString();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseUtil.failResult("验证码错误");
//        }
//        redisUtil.del(emailKey);
//        if (!value.equals(emailCode)){
//            return ResponseUtil.failResult("验证码错误");
//        }
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
            return ResponseUtil.successResult("注册成功");
        }else {
            return ResponseUtil.failResult(ResultCode.register_fail,"注册失败");
        }
    }

    @PostMapping("/getEmailCode")
    public Result GetCode(@RequestBody String email, HttpServletRequest request, HttpServletResponse response){
        Map<String,Object> map= JSON.parseObject(email,Map.class);
        String mail= (String) map.get("email");
        String code=AuthCodeUtil.getUUID();
        String key=AuthCodeUtil.getUUID();
        redisUtil.set(key,code);
        response.setHeader("emailSession",key);
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
        mailService.sendWithHtml(mail,subject,content);
        redisUtil.expire(key,60*3);
        return ResponseUtil.successResult("发送成功");
    }

    @PostMapping("/emaliLogin")
    public Result login(@RequestBody String body,HttpServletResponse response,HttpServletRequest request){
        if(body==null){
            return ResponseUtil.failResult("参数传入失败");
        }
        Map<String,Object> map=JSON.parseObject(body,Map.class);
        String email= String.valueOf(map.get("email")) ;
        String code= String.valueOf(map.get("emailCode")) ;
        String emailkey=request.getHeader("emailSession");
        if (!code.equals(emailkey)){
            return ResponseUtil.failResult("验证码错误");
        }
        Users users=new Users();
        users.setEmail(email);
        return ResponseUtil.successResult("登录成功");
    }

}
