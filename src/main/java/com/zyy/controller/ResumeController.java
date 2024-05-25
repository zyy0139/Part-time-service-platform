package com.zyy.controller;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zyy.entity.Deliveries;
import com.zyy.entity.Resumes;
import com.zyy.entity.Users;
import com.zyy.service.impl.DeliveryServiceImpl;
import com.zyy.service.impl.RecruitServiceImpl;
import com.zyy.service.impl.ResumeServiceImpl;
import com.zyy.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.zyy.utils.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/zyy/resume")
@CrossOrigin(origins = "*")
public class ResumeController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ResumeServiceImpl resumeService;

    @Autowired
    private DeliveryServiceImpl deliveryService;

    @Autowired
    private RecruitServiceImpl recruitService;

    @PostMapping("/addResume")
    public Result addResume(@RequestBody String body, HttpServletRequest request){
        if(body==null){
            return ResponseUtils.failResult("参数传入错误");
        }
        Map<String,Object> map= JSON.parseObject(body,Map.class);
        String token= String.valueOf(request.getAttribute(JWTUtils.USER_TOKEN));
        DecodedJWT jwt=JWTUtils.verify(token);
        String userId=jwt.getSubject();
        String resumeId = RadomUtils.creatId();
        Resumes resume=new Resumes();
        resume.setResumeId(resumeId);
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
        List<Deliveries> deliveryList=deliveryService.getAllByCompanyId(companyId);
        if(deliveryList==null){
            return ResponseUtils.failResult(ResultCode.not_found,"暂无投递信息");
        }
        List<Map<String,Object>> mapList=new ArrayList<>();
        for (Deliveries deliveries : deliveryList) {
            String userId = deliveries.getUserId();
            String recruitId = deliveries.getRecruitId();
            Resumes resume = resumeService.getAllByUserId(userId);
            Users user = userService.SelectAllById(userId);
            String recruitName = recruitService.getCareerByRecruitId(recruitId);
            Map<String, Object> map = new HashMap<>();
            map.put("resumeId",resume.getResumeId());
            map.put("userId", userId);
            map.put("recruitId", recruitId);
            map.put("userName", user.getName());
            map.put("userSex", user.getSex());
            map.put("userAge", user.getAge());
            map.put("userAddress", user.getAddress());
            map.put("userSchool", user.getSchool());
            map.put("userPhone", user.getPhone());
            map.put("userProfession", user.getProfession());
            map.put("career", resume.getCareer());
            map.put("recruitName", recruitName);
            map.put("type", resume.getType());
            map.put("skill", resume.getSkill());
            map.put("experience", resume.getExperience());
            map.put("sendDate", deliveries.getSendDate());
            mapList.add(map);
        }
        PageHelper.startPage(Integer.parseInt(page),Integer.parseInt(pageSize));
        PageInfo<Map<String,Object>> resumeList=new PageInfo<>(mapList);
        int total=deliveryService.getNumByCompanyId(companyId);
        Map<String,Object> map1=new HashMap<>();
        map1.put("total",total);
        map1.put("resumeList",resumeList.getList());
        return ResponseUtils.successResult("查询成功",map1);
    }

    @GetMapping("/getResume")
    public Result getResume(HttpServletRequest request){ //学生用户使用
        String header = request.getHeader("Authorization");
        String token = header.substring(18);
        DecodedJWT jwt = JWTUtils.verify(token);
        String userId = jwt.getSubject();
        Resumes resume = resumeService.getAllByUserId(userId);
        if(resume==null){
            return ResponseUtils.failResult(ResultCode.not_found,"暂无简历信息");
        }
        Map<String,Object> map=new HashMap<>();
        map.put("resumeId",resume.getResumeId());
        map.put("userId",resume.getUserId());
        map.put("career",resume.getCareer());
        map.put("type",resume.getType());
        map.put("skill",resume.getSkill());
        map.put("experience",resume.getExperience());
        return ResponseUtils.successResult("查询成功",map);
    }

    @GetMapping("/getResumeInfo")
    public Result getResumeInfo(@RequestParam String resumeId,@RequestParam String recruitId, HttpServletRequest request){//企业用户使用
        String header = request.getHeader("Authorization");
        String token = header.substring(18);
        DecodedJWT jwt=JWTUtils.verify(token);
        String companyId=jwt.getSubject();
        Resumes resume = resumeService.getAllByResumeId(resumeId);
        String userId=resume.getUserId();
        Users user = userService.SelectAllById(userId);
        String recruitName = recruitService.getCareerByRecruitId(recruitId);
        Deliveries delivery = deliveryService.getMessage(userId, companyId,recruitId);
        Map<String,Object> map=new HashMap<>();
        map.put("resumeId",resumeId);
        map.put("userId",userId);
        map.put("userName", user.getName());
        map.put("userSex", user.getSex());
        map.put("userAge", user.getAge());
        map.put("userAddress", user.getAddress());
        map.put("userSchool", user.getSchool());
        map.put("userPhone", user.getPhone());
        map.put("userProfession", user.getProfession());
        map.put("career", resume.getCareer());
        map.put("recruitName", recruitName);
        map.put("type", resume.getType());
        map.put("skill", resume.getSkill());
        map.put("experience", resume.getExperience());
        map.put("sendDate", delivery.getSendDate());
        return ResponseUtils.successResult("查询成功",map);
    }

    @GetMapping("/getResumeListBySearch")
    public Result getResumeListBySearch(@RequestParam String career, @RequestParam String page, @RequestParam String pageSize, HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String token = header.substring(18);
        DecodedJWT jwt=JWTUtils.verify(token);
        String companyId=jwt.getSubject();
        List<Deliveries> deliveryList;
        String recruitId = "";
        if(career == null){
            deliveryList = deliveryService.getAllByCompanyId(companyId);
        }else {
            recruitId = recruitService.getRecruitIdByCareer(career);
            deliveryList = deliveryService.getAllByCompanyIdAndRecruitId(companyId, recruitId);
        }
        if(deliveryList==null){
            return ResponseUtils.failResult(ResultCode.not_found,"暂无投递信息");
        }
        List<Map<String,Object>> mapList=new ArrayList<>();
        for (Deliveries deliveries : deliveryList) {
            String userId = deliveries.getUserId();
            Resumes resume = resumeService.getAllByUserId(userId);
            Users user = userService.SelectAllById(userId);
            if(career == null){
                recruitId = deliveries.getRecruitId();
            }
            Map<String, Object> map = new HashMap<>();
            map.put("resumeId",resume.getResumeId());
            map.put("userId", userId);
            map.put("recruitId", recruitId);
            map.put("userName", user.getName());
            map.put("userSex", user.getSex());
            map.put("userAge", user.getAge());
            map.put("userAddress", user.getAddress());
            map.put("userSchool", user.getSchool());
            map.put("userPhone", user.getPhone());
            map.put("userProfession", user.getProfession());
            map.put("career", resume.getCareer());
            map.put("recruitName", career);
            map.put("type", resume.getType());
            map.put("skill", resume.getSkill());
            map.put("experience", resume.getExperience());
            map.put("sendDate", deliveries.getSendDate());
            mapList.add(map);
        }
        PageHelper.startPage(Integer.parseInt(page),Integer.parseInt(pageSize));
        PageInfo<Map<String,Object>> resumeList=new PageInfo<>(mapList);
        int total=deliveryService.getNumByCompanyIdAndRecruitId(companyId,recruitId);
        Map<String,Object> map1=new HashMap<>();
        map1.put("total",total);
        map1.put("resumeList",resumeList.getList());
        return ResponseUtils.successResult("查询成功",map1);
    }

    @GetMapping("/getByTodayRecommend")
    public Result getByTodayRecommend(HttpServletRequest request){ //今日推荐
        Date today = DateUtils.getNow();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String todayStr = sdf.format(today);
        Date date = null;
        try{
            DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate=LocalDate.parse(todayStr,formatter);
            localDate=localDate.minusDays(1);
            date= java.sql.Date.valueOf(localDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String header = request.getHeader("Authorization");
        String token = header.substring(18);
        DecodedJWT jwt=JWTUtils.verify(token);
        String companyId=jwt.getSubject();
        List<Deliveries> deliveryList=deliveryService.getAllBySendDate(date,companyId);
        List<Map<String,Object>> mapList=new ArrayList<>();
        for (Deliveries deliveries : deliveryList) {
            String userId = deliveries.getUserId();
            String recruitId = deliveries.getRecruitId();
            Resumes resume = resumeService.getAllByUserId(userId);
            Users user = userService.SelectAllById(userId);
            String recruitName = recruitService.getCareerByRecruitId(recruitId);
            Map<String, Object> map = new HashMap<>();
            map.put("resumeId",resume.getResumeId());
            map.put("userId", userId);
            map.put("recruitId", recruitId);
            map.put("userName", user.getName());
            map.put("userSex", user.getSex());
            map.put("userAge", user.getAge());
            map.put("userAddress", user.getAddress());
            map.put("userSchool", user.getSchool());
            map.put("userPhone", user.getPhone());
            map.put("userProfession", user.getProfession());
            map.put("career", resume.getCareer());
            map.put("recruitName", recruitName);
            map.put("type", resume.getType());
            map.put("skill", resume.getSkill());
            map.put("experience", resume.getExperience());
            map.put("sendDate", deliveries.getSendDate());
            mapList.add(map);
        }
        int total= deliveryList.size();
        Map<String,Object> map1=new HashMap<>();
        map1.put("total",total);
        map1.put("resumeList",mapList);
        return ResponseUtils.successResult("查询成功",map1);
    }

}
