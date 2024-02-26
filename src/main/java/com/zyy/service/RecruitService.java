package com.zyy.service;

import com.github.pagehelper.PageInfo;
import com.zyy.entity.Recruits;

public interface RecruitService {
    public int sendRecruit(Recruits recruits);
    public int deleteByRecruitIdAndCompanyId(String recruitId,String companyId);
    public int updateByRecruitIdAndCompanyId(Recruits recruits);
    public Recruits selectByRecruitIdAndCompanyId(String recruitId,String companyId);
    public PageInfo<Recruits> selectAll(int page,int pageSize);
    public int selectRecruitNum();
}
