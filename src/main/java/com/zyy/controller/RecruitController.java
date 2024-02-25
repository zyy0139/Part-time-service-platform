package com.zyy.controller;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zyy.entity.Recruits;
import com.zyy.service.impl.RecruitServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.zyy.utils.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/zyy/recruit")
@CrossOrigin(origins = "*")
public class RecruitController {

    @Autowired
    private RecruitServiceImpl recruitService;

    @PostMapping("/sendRecruit")
    public Result sendRecruit(@RequestBody String body, HttpServletRequest request){
        if(body==null){
            return ResponseUtils.failResult("传入参数失败");
        }
        Map<String,Object> map= JSON.parseObject(body,Map.class);
        Recruits recruit=new Recruits();
        String token= String.valueOf(request.getAttribute(JWTUtils.USER_TOKEN));
        DecodedJWT jwt=JWTUtils.verify(token);
        String companyId=jwt.getSubject();
        String recruitId=RadomUtils.creatId();
        recruit.setRecruitId(recruitId);
        recruit.setCompanyId(companyId);
        recruit.setCareer((String) map.get("career"));
        recruit.setNumber((Integer) map.get("number"));
        recruit.setMessage((String)map.get("message"));
        recruit.setSalary((Integer) map.get("salary"));
        recruit.setFreefl((Boolean) map.get("freefl"));
        recruit.setInform((String) map.get("inform"));
        int result=recruitService.sendRecruit(recruit);
        if(result==1){
            return ResponseUtils.successResult("发布成功");
        }else{
            return ResponseUtils.failResult(ResultCode.add_fail,"发布失败");
        }
    }

    @DeleteMapping("/deleteRecruit")
    public Result deleteRecruit(@RequestParam String recruitId,HttpServletRequest request){
        if(recruitId==null){
            return ResponseUtils.failResult("参数传入失败");
        }
        String token=String.valueOf(request.getAttribute(JWTUtils.USER_TOKEN));
        DecodedJWT jwt=JWTUtils.verify(token);
        String companyId=jwt.getSubject();
        int result=recruitService.deleteByRecruitIdAndCompanyId(recruitId,companyId);
        if(result==1){
            return ResponseUtils.successResult("删除成功");
        }else {
            return ResponseUtils.failResult(ResultCode.delete_fail,"删除失败");
        }
    }

}
