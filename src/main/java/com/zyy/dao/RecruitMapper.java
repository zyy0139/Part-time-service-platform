package com.zyy.dao;

import com.zyy.entity.Recruits;

import java.util.List;

public interface RecruitMapper {
    public int sendRecruit(Recruits recruits);
    public int deleteByRecruitIdAndCompanyId(String recruitId,String companyId);
    public int updateByRecruitIdAndCompanyId(Recruits recruits);
    public Recruits selectByRecruitIdAndCompanyId(String recruitId,String companyId);
    public List<Recruits> selectAll();
    public int selectRecruitNum();
    public int updateNumber(String recruitId,int num);
    public List<Recruits> selectAllByType(String type);
    public int selectRecruitNumByType(String type);
}
