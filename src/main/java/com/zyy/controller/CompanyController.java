package com.zyy.controller;

import com.alibaba.fastjson.JSON;
import com.zyy.entity.Companies;
import com.zyy.service.impl.CompanyServiceImpl;
import com.zyy.utils.*;
import com.zyy.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/zyy/company")
@CrossOrigin(origins = "*")
public class CompanyController {
    @Autowired
    private CompanyServiceImpl companyService;

    @Autowired
    private RedisUtils redisUtil;


    @PostMapping("/register")
    public Result companyRegister(@RequestBody String body, HttpServletRequest request){
        Map<String,Object> map= JSON.parseObject(body,Map.class);
        String email=String.valueOf(map.get("email"));
        Companies company=companyService.selectAllByEmail(email);
        if(company==null){
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
        Companies companies=new Companies();
        String id=RadomUtils.creatId();
        String account=String.valueOf(map.get("account"));
        String password=String.valueOf(map.get("password"));
        String md5_Password=MD5Utils.getMD5(password);
        companies.setId(id);
        companies.setAccount(account);
        companies.setEmail(email);
        companies.setPassword(md5_Password);
        int result=companyService.companyRegister(companies);
        if(result==1){
            return ResponseUtils.successResult("注册成功");
        }else {
            return ResponseUtils.failResult(ResultCode.register_fail,"注册失败");
        }
    }

    @PostMapping("/emailLogin")
    public Result emailLogin(@RequestBody String body, HttpServletRequest request, HttpServletResponse response){
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
        Companies companies=new Companies();
        companies.setEmail(email);
        Map map1=companyService.token(companies);
        if(map1.get("code").equals("0")){
            return ResponseUtils.failResult("邮箱输入错误");
        }
        response.setHeader("Authorization",JWTUtils.USER_TOKEN+map1.get("token"));
        return ResponseUtils.successResult("登录成功");
    }

    @PostMapping("/accountLogin")
    public Result accountLogin(@RequestBody String body,HttpServletRequest request,HttpServletResponse response){
        if(body==null){
            return ResponseUtils.failResult("参数传入失败");
        }
        Map<String,Object> map=JSON.parseObject(body,Map.class);
        String account=String.valueOf(map.get("account"));
        String password=String.valueOf(map.get("password"));
        String md5_Password=MD5Utils.getMD5(password);
        Companies companies=new Companies();
        companies.setAccount(account);
        companies.setPassword(md5_Password);
        Map map1=companyService.token(companies);
        if(map1.get("code").equals("0")){
            return ResponseUtils.failResult("邮箱输入错误");
        }
        response.setHeader("Authorization",JWTUtils.USER_TOKEN+map1.get("token"));
        return ResponseUtils.successResult("登录成功");
    }
}
