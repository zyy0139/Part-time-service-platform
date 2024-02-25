package com.zyy.service;

import com.zyy.entity.Recruits;

public interface RecruitService {
    public int sendRecruit(Recruits recruits);
    public int deleteByRecruitIdAndCompanyId(String recruitId,String companyId);
}
