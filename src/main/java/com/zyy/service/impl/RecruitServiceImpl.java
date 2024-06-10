package com.zyy.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zyy.dao.CompanyMapper;
import com.zyy.dao.RecruitMapper;
import com.zyy.entity.Companies;
import com.zyy.entity.Recruits;
import com.zyy.service.RecruitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class RecruitServiceImpl implements RecruitService {

    @Autowired
    private RecruitMapper recruitMapper;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private WorkTypeServiceImpl workTypeService;

    @Override
    public int sendRecruit(Recruits recruits) {
        int result=recruitMapper.sendRecruit(recruits);
        return result;
    }

    @Override
    public int deleteByRecruitIdAndCompanyId(String recruitId, String companyId) {
        int result=recruitMapper.deleteByRecruitIdAndCompanyId(recruitId,companyId);
        return result;
    }

    @Override
    public int updateByRecruitIdAndCompanyId(Recruits recruits) {
        int result=recruitMapper.updateByRecruitIdAndCompanyId(recruits);
        return result;
    }

    @Override
    public Recruits selectByRecruitIdAndCompanyId(String recruitId, String companyId) {
        Recruits recruit=recruitMapper.selectByRecruitIdAndCompanyId(recruitId,companyId);
        return recruit;
    }

    @Override
    public Recruits selectAllByRecruitId(String recruitId) {
        return recruitMapper.selectAllByRecruitId(recruitId);
    }

    @Override
    public PageInfo<Recruits> selectAll(int page,int pageSize) {
        PageHelper.startPage(page,pageSize);
        List<Recruits> list=recruitMapper.selectAll();
        PageInfo<Recruits> recruits=new PageInfo<>(list);
        return recruits;
    }

    @Override
    public int selectRecruitNum() {
        int num=recruitMapper.selectRecruitNum();
        return num;
    }

    @Override
    public int getNumber(String recruitId) {
        int number=recruitMapper.getNumber(recruitId);
        return number;
    }

    @Override
    public int updateNumber(String recruitId, int num) {
        int result=recruitMapper.updateNumber(recruitId,num);
        return result;
    }

    @Override
    public PageInfo<Recruits> selectAllBySearch(String address, Date releaseDate, String type, int page, int pageSize) {
        PageHelper.startPage(page,pageSize);
        List<Recruits> list=new ArrayList<>();
        if(!address.isEmpty()){
            List<String> companyIdList=companyMapper.selectIdByAddress(address);
            if(releaseDate==null && type.isEmpty()){
                for(String companyId:companyIdList){
                    list.addAll(recruitMapper.selectAllByCompanyId(companyId));
                }
            }else if(type.isEmpty()){
                for(String companyId:companyIdList){
                    list.addAll(recruitMapper.selectAllByCompanyIdAndReleaseDate(companyId,releaseDate));
                }
            }else if(releaseDate==null){
                for(String companyId:companyIdList){
                    list.addAll(recruitMapper.selectAllByCompanyIdAndType(companyId,type));
                }
            }else{
                for(String companyId:companyIdList){
                    list.addAll(recruitMapper.selectAllByCompanyIdAndTypeAndReleaseDate(companyId,type,releaseDate));
                }
            }
        } else if (releaseDate==null && type.isEmpty()) {
            list=recruitMapper.selectAll();
        } else if (releaseDate==null) {
            list=recruitMapper.selectAllByType(type);
        } else if (type.isEmpty()) {
            list=recruitMapper.selectAllByReleaseDate(releaseDate);
        } else{
            list=recruitMapper.selectAllByTypeAndReleaseDate(type,releaseDate);
        }
        return new PageInfo<>(list);
    }

    @Override
    public int selectRecruitNumByType(String type) {
        int num=recruitMapper.selectRecruitNumByType(type);
        return num;
    }

    @Override
    public PageInfo<Recruits> selectAllByCompanyId(String companyId, int page, int pageSize) {
        PageHelper.startPage(page,pageSize);
        List<Recruits> list=recruitMapper.selectAllByCompanyId(companyId);
        PageInfo<Recruits> recruits=new PageInfo<>(list);
        return recruits;
    }

    @Override
    public int getNumByCompanyId(String companyId) {
        int num=recruitMapper.selectNumByCompanyId(companyId);
        return num;
    }

    @Override
    public String getCareerByRecruitId(String recruitId) {
        String career=recruitMapper.getCareerByRecruitId(recruitId);
        return career;
    }

    @Override
    public String getRecruitIdByCareer(String career) {
        String recruitId=recruitMapper.getRecruitIdByCareer(career);
        return recruitId;
    }

    @Override
    public List<String> getCareerList(String companyId) {
        List<String> careerList=recruitMapper.getCareerListByCompanyId(companyId);
        return careerList;
    }

    @Override
    public List<Map<String, Object>> getRecruitCountList(String start, String end) {
        Date startDate;
        Date endDate;
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startLocalDate=LocalDate.parse(start,formatter);
        LocalDate endLocalDate=LocalDate.parse(end,formatter);
        startDate= java.sql.Date.valueOf(startLocalDate);
        endDate= java.sql.Date.valueOf(endLocalDate);
        int total = recruitMapper.getNumByStartAndEnd(startDate,endDate); // 统计总数量
        List<Map<String, Object>> list=new ArrayList<>();
        while (startDate.before(endDate)){
            int count = recruitMapper.getNumByReleaseDate(startDate); // 统计当天发布的数量
            Map<String, Object> map=new HashMap<>();
            map.put("date",startDate);
            map.put("newCount",count);
            map.put("allCount",total);
            list.add(map);
            LocalDate newStartLocalDate = startLocalDate.plusDays(1);// 日期加一天
            startLocalDate = newStartLocalDate;
            startDate = java.sql.Date.valueOf(newStartLocalDate);
        }
        int count = recruitMapper.getNumByReleaseDate(endDate); // 最后一天的数量
        Map<String, Object> map=new HashMap<>();
        map.put("date",endDate);
        map.put("newCount",count);
        map.put("allCount",total);
        list.add(map);
        return list;
    }

    @Override
    public List<Map<String, Object>> getRecruitCountByType(){
        List<String> typeList=workTypeService.getType();
        List<Map<String, Object>> list=new ArrayList<>();
        for(String type:typeList){
            int count = recruitMapper.getNumByType(type); // 统计该类型信息的数量
            Map<String, Object> map=new HashMap<>();
            map.put("name",type);
            map.put("value",count);
            list.add(map);
        }
        return list;
    }

    @Override
    public List<Recruits> selectAllByReleaseDate(Date releaseDate) {
        return recruitMapper.selectAllByReleaseDate(releaseDate);
    }
}
