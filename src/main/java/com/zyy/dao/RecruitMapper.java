package com.zyy.dao;

import com.zyy.entity.Recruits;

public interface RecruitMapper {
    public int sendRecruit(Recruits recruits);
    public int deleteByRecruitIdAndCompanyId(String recruitId,String companyId);
}
