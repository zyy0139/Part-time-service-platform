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
import java.util.Map;
import java.util.Random;

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
    public Result CompanyRegister(@RequestBody String body, HttpServletRequest request){
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
        companies.setId(id);
        companies.setAccount(account);
        companies.setEmail(email);
        companies.setPassword(password);
        int result=companyService.companyRegister(companies);
        if(result==1){
            return ResponseUtils.successResult("注册成功");
        }else {
            return ResponseUtils.failResult(ResultCode.register_fail,"注册失败");
        }
    }
}