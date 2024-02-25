package com.zyy.service.impl;

import com.zyy.dao.RecruitMapper;
import com.zyy.entity.Recruits;
import com.zyy.service.RecruitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
