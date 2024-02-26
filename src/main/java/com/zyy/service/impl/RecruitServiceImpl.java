package com.zyy.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zyy.dao.RecruitMapper;
import com.zyy.entity.Recruits;
import com.zyy.service.RecruitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecruitServiceImpl implements RecruitService {

    @Autowired
    private RecruitMapper recruitMapper;

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
}
