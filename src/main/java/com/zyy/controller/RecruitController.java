package com.zyy.controller;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.github.pagehelper.PageInfo;
import com.zyy.entity.Companies;
import com.zyy.entity.Recruits;
import com.zyy.service.impl.CompanyServiceImpl;
import com.zyy.service.impl.RecruitServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.zyy.utils.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/zyy/recruit")
@CrossOrigin(origins = "*")
public class RecruitController {

    @Autowired
    private RecruitServiceImpl recruitService;

    @Autowired
    private CompanyServiceImpl companyService;

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
        Date releaseDate=DateUtils.getNow();
        recruit.setRecruitId(recruitId);
        recruit.setCompanyId(companyId);
        recruit.setCareer((String) map.get("career"));
        recruit.setType((String) map.get("type"));
        recruit.setNumber((Integer) map.get("number"));
        recruit.setMessage((String)map.get("message"));
        recruit.setSalary((Integer) map.get("salary"));
        recruit.setFreefl((Boolean) map.get("freefl"));
        recruit.setReleaseDate(releaseDate);
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
        String header= request.getHeader("Authorization");
        String token=header.substring(18);
        DecodedJWT jwt=JWTUtils.verify(token);
        String companyId=jwt.getSubject();
        int result=recruitService.deleteByRecruitIdAndCompanyId(recruitId,companyId);
        if(result==1){
            return ResponseUtils.successResult("删除成功");
        }else {
            return ResponseUtils.failResult(ResultCode.delete_fail,"删除失败");
        }
    }

    @PostMapping("/updateRecruit")
    public Result updateRecruit(@RequestParam String recruitId,@RequestBody String body,HttpServletRequest request){
        String header= request.getHeader("Authorization");
        String token=header.substring(18);
        DecodedJWT jwt=JWTUtils.verify(token);
        String companyId=jwt.getSubject();
        Map<String,Object> map=JSON.parseObject(body,Map.class);
        Date releaseDate=DateUtils.getNow();
        Recruits recruit=new Recruits();
        recruit.setRecruitId(recruitId);
        recruit.setCompanyId(companyId);
        recruit.setCareer((String) map.get("career"));
        recruit.setType((String) map.get("type"));
        recruit.setNumber((Integer) map.get("number"));
        recruit.setMessage((String) map.get("message"));
        recruit.setSalary((Integer) map.get("salary"));
        recruit.setFreefl((Boolean) map.get("freefl"));
        recruit.setReleaseDate(releaseDate);
        int result=recruitService.updateByRecruitIdAndCompanyId(recruit);
        if(result==1){
            return ResponseUtils.successResult("修改成功");
        }else {
            return ResponseUtils.failResult(ResultCode.update_fail,"修改失败");
        }
    }

    @GetMapping("/getMessage")
    public Result getMessage(@RequestParam String recruitId,@RequestParam String companyId){
        Map<String,Object> map=new HashMap<>();
        Recruits recruit=recruitService.selectByRecruitIdAndCompanyId(recruitId,companyId);
        if(recruit==null){
            return ResponseUtils.failResult("查询失败");
        }
        map.put("recruitId",recruit.getRecruitId());
        map.put("career",recruit.getCareer());
        map.put("type",recruit.getType());
        map.put("number",recruit.getNumber());
        map.put("message",recruit.getMessage());
        map.put("salary",recruit.getSalary());
        map.put("freefl",recruit.isFreefl());
        map.put("releaseDate",recruit.getReleaseDate());
        return ResponseUtils.successResult("查询成功",map);
    }

    @GetMapping("/getMessageList")
    public Result getMessageList(@RequestParam String page, @RequestParam String pageSize){
        PageInfo<Recruits> list=recruitService.selectAll(Integer.parseInt(page),Integer.parseInt(pageSize));
        if(list.getSize()==0){
            return ResponseUtils.failResult("暂无招聘信息");
        }
        List<Map<String,Object>> mapList=new ArrayList<>();
        List<Recruits> recruitsList=list.getList();
        for(int i=0;i<list.getSize();i++){
            Map<String,Object> map=new HashMap<>();
            Companies company=companyService.selectAllById(recruitsList.get(i).getCompanyId());
            map.put("recruitId",recruitsList.get(i).getRecruitId());
            map.put("companyName",company.getName());
            map.put("companyAddress",company.getAddress());
            map.put("companyEmail",company.getEmail());
            map.put("companyPhone",company.getPhone());
            map.put("career",recruitsList.get(i).getCareer());
            map.put("type",recruitsList.get(i).getType());
            map.put("number",recruitsList.get(i).getNumber());
            map.put("message",recruitsList.get(i).getMessage());
            map.put("salary",recruitsList.get(i).getSalary());
            map.put("freefl",recruitsList.get(i).isFreefl());
            map.put("releaseDate",recruitsList.get(i).getReleaseDate());
            mapList.add(map);
        }
        int total= recruitService.selectRecruitNum();
        Map<String,Object> map1=new HashMap<>();
        map1.put("total",total);
        map1.put("recruitList",mapList);
        return ResponseUtils.successResult("查询成功",map1);
    }

    @GetMapping("/getMessageBySearch")
    public Result getMessageBySearch(@RequestParam String address,@RequestParam String releaseDate,
                                      @RequestParam String type,@RequestParam String page,@RequestParam String pageSize){
        Date date;
        if(releaseDate.isEmpty()){
            date=null;
        }else {
            DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate=LocalDate.parse(releaseDate,formatter);
            date= java.sql.Date.valueOf(localDate);
        }
        PageInfo<Recruits> list=recruitService.selectAllBySearch(address,date,type,Integer.parseInt(page),Integer.parseInt(pageSize));
        if(list.getSize()==0){
            return ResponseUtils.failResult(ResultCode.select_fail,"暂无该类型的招聘信息");
        }
        List<Map<String,Object>> mapList=new ArrayList<>();
        List<Recruits> recruitsList=list.getList();
        for(int i=0;i<list.getSize();i++){
            Map<String,Object> map=new HashMap<>();
            Companies company=companyService.selectAllById(recruitsList.get(i).getCompanyId());
            map.put("recruitId",recruitsList.get(i).getRecruitId());
            map.put("companyName",company.getName());
            map.put("companyAddress",company.getAddress());
            map.put("companyEmail",company.getEmail());
            map.put("companyPhone",company.getPhone());
            map.put("career",recruitsList.get(i).getCareer());
            map.put("type",recruitsList.get(i).getType());
            map.put("number",recruitsList.get(i).getNumber());
            map.put("message",recruitsList.get(i).getMessage());
            map.put("salary",recruitsList.get(i).getSalary());
            map.put("freefl",recruitsList.get(i).isFreefl());
            map.put("releaseDate",recruitsList.get(i).getReleaseDate());
            mapList.add(map);
        }
        int total= list.getSize();
        Map<String,Object> map1=new HashMap<>();
        map1.put("total",total);
        map1.put("recruitList",mapList);
        return ResponseUtils.successResult("查询成功",map1);
    }

    @GetMapping("/getByCompanyName")
    public Result getByCompanyName(@RequestParam String companyName,@RequestParam String page,@RequestParam String pageSize){
        String companyId=companyService.getIdByName(companyName);
        PageInfo<Recruits> list=recruitService.selectAllByCompanyId(companyId,Integer.parseInt(page),Integer.parseInt(pageSize));
        if(list.getSize()==0){
            return ResponseUtils.failResult(ResultCode.select_fail,"暂无该公司的招聘信息");
        }
        List<Map<String,Object>> mapList=new ArrayList<>();
        List<Recruits> recruitsList=list.getList();
        for(int i=0;i<list.getSize();i++){
            Map<String,Object> map=new HashMap<>();
            Companies company=companyService.selectAllById(recruitsList.get(i).getCompanyId());
            map.put("recruitId",recruitsList.get(i).getRecruitId());
            map.put("companyName",company.getName());
            map.put("companyAddress",company.getAddress());
            map.put("companyEmail",company.getEmail());
            map.put("companyPhone",company.getPhone());
            map.put("career",recruitsList.get(i).getCareer());
            map.put("type",recruitsList.get(i).getType());
            map.put("number",recruitsList.get(i).getNumber());
            map.put("message",recruitsList.get(i).getMessage());
            map.put("salary",recruitsList.get(i).getSalary());
            map.put("freefl",recruitsList.get(i).isFreefl());
            map.put("releaseDate",recruitsList.get(i).getReleaseDate());
            mapList.add(map);
        }
        int total=recruitService.getNumByCompanyId(companyId);
        Map<String,Object> map1=new HashMap<>();
        map1.put("total",total);
        map1.put("recruitList",mapList);
        return ResponseUtils.successResult("查询成功",map1);
    }

}
