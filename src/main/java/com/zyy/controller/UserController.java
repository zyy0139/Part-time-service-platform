package com.zyy.controller;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zyy.entity.Users;
import com.zyy.service.impl.UserServiceImpl;
import com.zyy.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/zyy/user")
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private UserServiceImpl userService;

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
        Object emailKey=redisUtil.get(email);
        if (emailKey==null || !emailKey.toString().equals(emailCode)){
            return ResponseUtils.failResult("验证码错误");
        }else {
            redisUtil.del(email);
        }
        Users user=new Users();
        String id=RadomUtils.creatId();
        String account=String.valueOf(map.get("account"));
        String password=String.valueOf(map.get("password"));
        String md5_Password=MD5Utils.getMD5(password);
        user.setId(id);
        user.setAccount(account);
        user.setPassword(md5_Password);
        user.setEmail(email);
        int result=userService.Regist(user);
        if(result==1){
            return ResponseUtils.successResult("注册成功");
        }else {
            return ResponseUtils.failResult(ResultCode.register_fail,"注册失败");
        }
    }

    @PostMapping("/emailLogin")
    public Result login(@RequestBody String body, HttpServletResponse response){
        if(body==null){
            return ResponseUtils.failResult("参数传入失败");
        }
        Map<String,Object> map=JSON.parseObject(body,Map.class);
        String email= String.valueOf(map.get("email")) ;
        String code= String.valueOf(map.get("emailCode"));
        Object key=redisUtil.get(email);
        if (key==null || !key.toString().equals(code)){
            return ResponseUtils.failResult("验证码错误");
        }else {
            redisUtil.del(email);
        }
        Users users=new Users();
        users.setEmail(email);
        Map map1=userService.token(users);
        if(map1.get("code").equals("0")){
            return ResponseUtils.failResult("邮箱输入错误");
        }
        response.setHeader("Authorization",JWTUtils.USER_TOKEN+"="+map1.get("token"));
        return ResponseUtils.successResult("登录成功",map1.get("user"));
    }

    @PostMapping("/accountLogin")
    public Result login2(@RequestBody String body,HttpServletResponse response){
        if(body==null){
            return ResponseUtils.failResult("参数传入失败");
        }
        Map<String ,Object> map=JSON.parseObject(body,Map.class);
        String account= String.valueOf(map.get("account"));
        String password=String.valueOf(map.get("password"));
        String md5_Password=MD5Utils.getMD5(password);
        Users users=new Users();
        users.setAccount(account);
        users.setPassword(md5_Password);
        Map map1=userService.token(users);
        if(map1.get("code").equals("0")){
            return ResponseUtils.failResult("用户名或密码错误");
        }
        response.setHeader("Authorization",JWTUtils.USER_TOKEN+"="+map1.get("token"));
        return ResponseUtils.successResult("登陆成功",map1.get("user"));
    }

    @GetMapping("/getUserMessage")
    public Result getUserMessage(HttpServletRequest request,@RequestBody String body){
        String token= String.valueOf(request.getAttribute(JWTUtils.USER_TOKEN));
        DecodedJWT jwt=JWTUtils.verify(token);
        String userId=jwt.getSubject();
        Users users=userService.SelectAllById(userId);
        if(users==null){
            return ResponseUtils.failResult("查询失败");
        }
        Map<String,Object> map=new HashMap<>();
        map.put("userName",users.getName());
        map.put("userSex",users.getSex());
        map.put("userAge",users.getAge());
        map.put("userAddress",users.getAddress());
        map.put("userSchool",users.getSchool());
        map.put("userProfession",users.getProfession());
        map.put("userPhone",users.getPhone());
        return ResponseUtils.successResult("查询成功",map);
    }

    @PostMapping("/updateByUserId")
    public Result updateByUserId(@RequestBody Users users,HttpServletRequest request){
        String token=String.valueOf(request.getAttribute(JWTUtils.USER_TOKEN));
        DecodedJWT jwt=JWTUtils.verify(token);
        String userId=jwt.getSubject();
        users.setId(userId);
        users.setIsAdmit(false);
        int result=userService.UpdateAllById(users);
        if(result==1){
            return ResponseUtils.successResult("修改成功");
        }else {
            return ResponseUtils.failResult("修改失败");
        }
    }

    @GetMapping("/getAdmit")
    public Result getAdmit(HttpServletRequest request){
        String token=String.valueOf(request.getAttribute(JWTUtils.USER_TOKEN));
        if(token==null){
            return ResponseUtils.failResult("无法解析到token");
        }
        DecodedJWT jwt=JWTUtils.verify(token);
        String userId=jwt.getSubject();
        Users user=userService.SelectAllById(userId);
        Boolean admit=user.getIsAdmit();
        Map<String,Object> map=new HashMap<>();
        map.put("userId",userId);
        map.put("isAdmit",admit);
        return ResponseUtils.successResult("查询成功",map);
    }

}
