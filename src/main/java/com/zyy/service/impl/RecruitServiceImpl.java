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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class RecruitServiceImpl implements RecruitService {

    @Autowired
    private RecruitMapper recruitMapper;

    @Autowired
    private CompanyMapper companyMapper;

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
        PageHelper.startPage(page,pageSize);
        PageInfo<Recruits> recruits=new PageInfo<>(list);
        return recruits;
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
}
