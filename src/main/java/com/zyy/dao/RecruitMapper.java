package com.zyy.dao;

import com.zyy.entity.Recruits;

import java.util.Date;
import java.util.List;

public interface RecruitMapper {
    public int sendRecruit(Recruits recruits);
    public int deleteByRecruitIdAndCompanyId(String recruitId,String companyId);
    public int updateByRecruitIdAndCompanyId(Recruits recruits);
    public Recruits selectByRecruitIdAndCompanyId(String recruitId,String companyId);
    public Recruits selectAllByRecruitId(String recruitId);
    public List<Recruits> selectAll();
    public int selectRecruitNum();
    public int updateNumber(String recruitId,int number);
    public int getNumber(String recruitId);
    public List<Recruits> selectAllByType(String type);
    public List<Recruits> selectAllByReleaseDate(Date releaseDate);
    public int selectRecruitNumByType(String type);
    public List<Recruits> selectAllByCompanyId(String companyId);
    public int selectNumByCompanyId(String companyId);
    public List<Recruits> selectAllByCompanyIdAndType(String companyId,String type);
    public List<Recruits> selectAllByCompanyIdAndReleaseDate(String companyId, Date releaseDate);
    public List<Recruits> selectAllByTypeAndReleaseDate(String type,Date releaseDate);
    public List<Recruits> selectAllByCompanyIdAndTypeAndReleaseDate(String companyId,String type,Date releaseDate);
    public String getCareerByRecruitId(String recruitId);
    public String getRecruitIdByCareer(String career);
    public List<String> getCareerListByCompanyId(String companyId);
    public int getNumByStartAndEnd(Date start,Date end);
    public int getNumByReleaseDate(Date releaseDate);
    public int getNumByType(String type);
}
