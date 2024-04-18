package com.zyy.dao;

import com.zyy.entity.Deliveries;

import java.util.Date;
import java.util.List;

public interface DeliveryMapper {
    public List<Deliveries> getAllByCompanyId(String companyId);
    public List<Deliveries> getAllByCompanyIdAndRecruitId(String companyId, String recruitId);
    public int getNumByCompanyId(String companyId);
    public int getNumByCompanyIdAndRecruitId(String companyId, String recruitId);
    public int addDelivery(Deliveries deliveries);
    public int deleteDelivery(String userId,String companyId,String recruitId);
    public String getRecruitId(String userId,String companyId);
    public Deliveries getMessage(String userId,String companyId,String recruitId);
    public int getCountByStartAndEnd(Date start, Date end, String companyId);
    public int getCountByDate(Date date,String companyId);
    public int getCountByRecruitId(String recruitId);
}
