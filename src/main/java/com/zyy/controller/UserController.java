package com.zyy.controller;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zyy.entity.Users;
import com.zyy.service.impl.MailServiceImpl;
import com.zyy.service.impl.UserServiceImpl;
import com.zyy.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
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
    private RedisUtils redisUtil;

    @PostMapping("/register")
    public Result Register(@RequestBody String body, HttpServletRequest request){
        Map<String,Object> map=JSON.parseObject(body,Map.class);
        String email= String.valueOf(map.get("email"));
        Users users=userService.SelectAllByEmail(email);
        if (users!=null){
            return ResponseUtils.failResult(ResultCode.USER_EXIST,"该邮箱已注册");
        }
        String emailCode=String.valueOf(map.get("emailCode"));
        String emailKey=request.getHeader("emailSession");
        String value= null;
        try {
            value = redisUtil.get(emailKey).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtils.failResult("验证码错误");
        }
        redisUtil.del(emailKey);
        if (!value.equals(emailCode)){
            return ResponseUtils.failResult("验证码错误");
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
            return ResponseUtils.successResult("注册成功");
        }else {
            return ResponseUtils.failResult(ResultCode.register_fail,"注册失败");
        }
    }

    @PostMapping("/getEmailCode")
    public Result GetCode(@RequestBody String email, HttpServletRequest request, HttpServletResponse response){
        Map<String,Object> map= JSON.parseObject(email,Map.class);
        String mail= (String) map.get("email");
        String code= AuthCodeUtils.getUUID();
        String key= AuthCodeUtils.getUUID();
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
        return ResponseUtils.successResult("发送成功");
    }

    @PostMapping("/emailLogin")
    public Result login(@RequestBody String body,HttpServletResponse response,HttpServletRequest request){
        if(body==null){
            return ResponseUtils.failResult("参数传入失败");
        }
        Map<String,Object> map=JSON.parseObject(body,Map.class);
        String email= String.valueOf(map.get("email")) ;
        String code= String.valueOf(map.get("emailCode")) ;
        String emailKey=request.getHeader("emailSession");
        String value=redisUtil.get(emailKey).toString();
        if (!value.equals(code)){
            return ResponseUtils.failResult("验证码错误");
        }
        Users users=new Users();
        users.setEmail(email);
        Map map1=userService.token(users);
        if(map1.get("code").equals("0")){
            return ResponseUtils.failResult("邮箱或密码错误");
        }
        response.setHeader("Authorization",JWTUtils.USER_TOKEN+map1.get("token"));
        return ResponseUtils.successResult("登录成功");
    }

    @PostMapping("/accountLogin")
    public Result login2(@RequestBody String body,HttpServletResponse response){
        if(body==null){
            return ResponseUtils.failResult("参数传入失败");
        }
        Map<String ,Object> map=JSON.parseObject(body,Map.class);
        String account= String.valueOf(map.get("account"));
        String password=String.valueOf(map.get("password"));
        Users users=new Users();
        users.setAccount(account);
        users.setPassword(password);
        Map map1=userService.token(users);
        if(map1.get("code").equals("0")){
            return ResponseUtils.failResult("用户名或密码错误");
        }
        response.setHeader("Authorization",JWTUtils.USER_TOKEN+map1.get("token"));
        return ResponseUtils.successResult("登陆成功");
    }

    @GetMapping("/getUserMessage")
    public Result getUserMessage(HttpServletRequest request){
        String token= String.valueOf(request.getAttribute(JWTUtils.USER_TOKEN));
        DecodedJWT jwt=JWTUtils.verify(token);
        String userId=jwt.getSubject();
        Users users=userService.SelectAllById(userId);
        Map<String,Object> map=new HashMap<>();
        map.put("userName",users.getName());
        map.put("userSex",users.getSex());
        map.put("userAge",users.getAge());
        map.put("userAddress",users.getAddress());
        map.put("userSchool",users.getSchool());
        map.put("userProfession",users.getProfession());
        map.put("userPhone",users.getPhone());
        return ResponseUtils.successResult("success",map);
    }

    @PostMapping("/updateByUserId")
    public Result updateByUserId(@RequestBody Users users,HttpServletRequest request){
        String token=String.valueOf(request.getAttribute(JWTUtils.USER_TOKEN));
        DecodedJWT jwt=JWTUtils.verify(token);
        String userId=jwt.getSubject();
        int result=userService.UpdateAllById(users,userId);
        if(result==1){
            return ResponseUtils.successResult("修改成功");
        }else {
            return ResponseUtils.failResult("保存失败");
        }
    }
}
