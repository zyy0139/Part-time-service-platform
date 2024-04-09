package com.zyy.controller;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zyy.entity.Companies;
import com.zyy.service.impl.CompanyServiceImpl;
import com.zyy.utils.*;
import com.zyy.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
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
    public Result companyRegister(@RequestBody String body, HttpServletRequest request, HttpSession session){
        Map<String,Object> map= JSON.parseObject(body,Map.class);
        String email=String.valueOf(map.get("email"));
        Companies company=companyService.selectAllByEmail(email);
        if(company!=null){
            return ResponseUtils.failResult(ResultCode.USER_EXIST,"该邮箱已注册");
        }
        String emailCode=String.valueOf(map.get("emailCode"));
        Object emailKey=redisUtil.get(email);
        if(emailKey==null || !emailKey.toString().equals(emailCode)){
            return ResponseUtils.failResult("验证码错误");
        }else {
            redisUtil.del(email);
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
        Object key=redisUtil.get(email);
        if(key==null || !key.toString().equals(code)){
            return ResponseUtils.failResult("验证码错误");
        }else {
            redisUtil.del(email);
        }
        Companies companies=new Companies();
        companies.setEmail(email);
        Map map1=companyService.token(companies);
        if(map1.get("code").equals("0")){
            return ResponseUtils.failResult("邮箱输入错误");
        }
        response.setHeader("Access-Control-Expose-Headers", "Authorization");
        response.setHeader("Authorization",JWTUtils.USER_TOKEN+"="+map1.get("token"));
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
        response.setHeader("Access-Control-Expose-Headers", "Authorization");
        response.setHeader("Authorization",JWTUtils.USER_TOKEN+"="+map1.get("token"));
        return ResponseUtils.successResult("登录成功",map1.get("company"));
    }

    @PostMapping("/updateByCompanyId")
    public Result updateByCompanyId(@RequestBody String body,HttpServletRequest request){
        String header= request.getHeader("Authorization");
        String token=header.substring(18);
        DecodedJWT jwt=JWTUtils.verify(token);
        String companyId=jwt.getSubject();
        Map<String,Object> map=JSON.parseObject(body,Map.class);
        String name=String.valueOf(map.get("name"));
        String address=String.valueOf(map.get("address"));
        String phone=String.valueOf(map.get("phone"));
        String createDate=String.valueOf(map.get("createDate"));
        Companies companies=new Companies();
        companies.setName(name);
        companies.setAddress(address);
        companies.setPhone(phone);
        companies.setId(companyId);
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate=LocalDate.parse(createDate,formatter);
        Date date=java.sql.Date.valueOf(localDate);
        companies.setCreateDate(date);
        int result=companyService.updateById(companies);
        if(result==1){
            return ResponseUtils.successResult("修改成功");
        }else {
            return ResponseUtils.failResult("修改失败");
        }
    }

    @PostMapping("/getCompanyMessage")
    public Result getCompanyMessage(HttpServletRequest request){
        String header= request.getHeader("Authorization");
        String token=header.substring(18);
        DecodedJWT jwt=JWTUtils.verify(token);
        String companyId=jwt.getSubject();
        Companies company=companyService.selectAllById(companyId);
        if(company==null){
            return ResponseUtils.failResult("查询失败");
        }
        Map<String,Object> map=new HashMap<>();
        map.put("companyName",company.getName());
        map.put("companyAddress",company.getAddress());
        map.put("companyPhone",company.getPhone());
        map.put("companyCreateDate",company.getCreateDate());
        return ResponseUtils.successResult("查询成功",map);
    }

    @PostMapping("/updatePassword")
    public Result updatePassword(@RequestBody String body,HttpServletRequest request){
        String header= request.getHeader("Authorization");
        if(header==null){
            return ResponseUtils.failResult("未检测到token");
        }
        String token=header.substring(18);
        DecodedJWT jwt=JWTUtils.verify(token);
        String companyId=jwt.getSubject();
        Map<String,Object> map=JSON.parseObject(body,Map.class);
        String oldPassword=String.valueOf(map.get("oldPassword"));
        String newPassword=String.valueOf(map.get("newPassword"));
        String md5_oldPassword=MD5Utils.getMD5(oldPassword);
        Companies company=companyService.selectAllById(companyId);
        if(!company.getPassword().equals(md5_oldPassword)){
            return ResponseUtils.failResult("旧密码错误");
        }
        String md5_newPassword=MD5Utils.getMD5(newPassword);
        int result=companyService.updatePasswordById(companyId,md5_newPassword);
        if(result==1){
            return ResponseUtils.successResult("修改成功");
        }else {
            return ResponseUtils.failResult("修改失败");
        }

    }

    @GetMapping("/getName")
    public Result getName(HttpServletRequest request){
        String header= request.getHeader("Authorization");
        if(header==null){
            return ResponseUtils.failResult("未检测到token");
        }
        String token=header.substring(18);
        DecodedJWT jwt=JWTUtils.verify(token);
        String companyId=jwt.getSubject();
        Companies company=companyService.selectAllById(companyId);
        if(company==null){
            return ResponseUtils.failResult("查询失败");
        }
        Map<String,Object> map=new HashMap<>();
        map.put("companyId",company.getId());
        map.put("account",company.getAccount());
        map.put("companyName",company.getName());
        return ResponseUtils.successResult("查询成功",map);
    }

}
