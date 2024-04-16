package com.zyy.service;

import com.github.pagehelper.PageInfo;
import com.zyy.entity.Recruits;

import java.util.Date;
import java.util.List;

public interface RecruitService {
    public int sendRecruit(Recruits recruits);
    public int deleteByRecruitIdAndCompanyId(String recruitId,String companyId);
    public int updateByRecruitIdAndCompanyId(Recruits recruits);
    public Recruits selectByRecruitIdAndCompanyId(String recruitId,String companyId);
    public PageInfo<Recruits> selectAll(int page,int pageSize);
    public int selectRecruitNum();
    public int getNumber(String recruitId);
    public int updateNumber(String recruitId,int num);
    public PageInfo<Recruits> selectAllBySearch(String address, Date releaseDate, String type, int page, int pageSize);
    public int selectRecruitNumByType(String type);
    public PageInfo<Recruits> selectAllByCompanyId(String companyId,int page,int pageSize);
    public int getNumByCompanyId(String companyId);
    public String getCareerByRecruitId(String recruitId);
    public String getRecruitIdByCareer(String career);
    public List<String> getCareerList(String companyId);
}
