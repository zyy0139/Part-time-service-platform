package com.zyy.controller;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zyy.entity.Resumes;
import com.zyy.service.impl.DeliveryServiceImpl;
import com.zyy.service.impl.RecruitServiceImpl;
import com.zyy.service.impl.ResumeServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.zyy.utils.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/zyy/resume")
@CrossOrigin(origins = "*")
public class ResumeController {

    @Autowired
    private ResumeServiceImpl resumeService;

    @Autowired
    private DeliveryServiceImpl deliveryService;

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
        resume.setType((String) map.get("type"));
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

    @GetMapping("/getResumeList")
    public Result getResumeList(@RequestParam String page,@RequestParam String pageSize,HttpServletRequest request){
        String header = request.getHeader("Authorization");
        String token = header.substring(18);
        DecodedJWT jwt=JWTUtils.verify(token);
        String companyId=jwt.getSubject();
        List<String> userIdList=deliveryService.getUserIdByCompanyId(companyId);
        if(userIdList==null){
            return ResponseUtils.failResult("暂无投递信息");
        }
        List<Map<String,Object>> mapList=new ArrayList<>();
        for (int i=0;i<=userIdList.size();i++){
            Resumes resume=resumeService.getAllByUserId(userIdList.get(i));
            Map<String,Object> map=new HashMap<>();
            map.put("userId",resume.getUserId());
            map.put("career",resume.getCareer());
            map.put("type",resume.getType());
            map.put("skill",resume.getSkill());
            map.put("experience",resume.getExperience());
            mapList.add(map);
        }
        PageHelper.startPage(Integer.parseInt(page),Integer.parseInt(pageSize));
        PageInfo<Map<String,Object>> resumeList=new PageInfo<>(mapList);
        int total=deliveryService.getNumByCompanyId(companyId);
        Map<String,Object> map1=new HashMap<>();
        map1.put("total",total);
        map1.put("resumeList",resumeList);
        return ResponseUtils.successResult("查询成功",map1);
    }

    @GetMapping("/getResume")
    public Result getResume(HttpServletRequest request){
        String header = request.getHeader("Authorization");
        String token = header.substring(18);
        DecodedJWT jwt = JWTUtils.verify(token);
        String userId = jwt.getSubject();
        Resumes resume = resumeService.getAllByUserId(userId);
        if(resume==null){
            return ResponseUtils.failResult("暂无简历信息");
        }
        Map<String,Object> map=new HashMap<>();
        map.put("userId",resume.getUserId());
        map.put("career",resume.getCareer());
        map.put("type",resume.getType());
        map.put("skill",resume.getSkill());
        map.put("experience",resume.getExperience());
        return ResponseUtils.successResult("查询成功",map);
    }

}
