package com.zyy.controller;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zyy.entity.Resumes;
import com.zyy.service.impl.ResumeServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.zyy.utils.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/zyy/resume")
@CrossOrigin(origins = "*")
public class ResumeController {

    @Autowired
    private ResumeServiceImpl resumeService;

    @PostMapping("/addResume")
    public Result addResume(@RequestBody String body, HttpServletRequest request){
        if(body==null){
            return ResponseUtils.failResult("参数传入错误");
        }
        Map<String,Object> map= JSON.parseObject(body,Map.class);
        String token= String.valueOf(request.getAttribute(JWTUtils.USER_TOKEN));
        DecodedJWT jwt=JWTUtils.verify(token);
        String userId=jwt.getSubject();
        Resumes resume=new Resumes();
        resume.setUserId(userId);
        resume.setCareer((String) map.get("career"));
        resume.setSkill((String) map.get("skill"));
        resume.setExperience((String) map.get("experience"));
        int result=resumeService.addResume(resume);
        if(result==1){
            return ResponseUtils.successResult("添加成功");
        }else {
            return ResponseUtils.failResult(ResultCode.add_fail,"添加失败");
        }
    }

    @PostMapping("/updateResume")
    public Result updateResume(@RequestBody Resumes resumes,HttpServletRequest request){
        if(resumes==null){
            return ResponseUtils.failResult("参数传入错误");
        }
        String token= String.valueOf(request.getAttribute(JWTUtils.USER_TOKEN));
        DecodedJWT jwt=JWTUtils.verify(token);
        String userId=jwt.getSubject();
        resumes.setUserId(userId);
        int result=resumeService.updateResume(resumes);
        if(result==1){
            return ResponseUtils.successResult("修改成功");
        }else {
            return ResponseUtils.failResult(ResultCode.update_fail,"修改失败");
        }
    }

}
