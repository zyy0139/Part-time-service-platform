package com.zyy.service;

import com.github.pagehelper.PageInfo;
import com.zyy.entity.Deliveries;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface DeliveryService {
    public PageInfo<Deliveries> getAllByCompanyId(String companyId,int page, int pageSize);
    public PageInfo<Deliveries> getAllByCompanyIdAndRecruitId(String companyId, String recruiterId, int page, int pageSize);
    public int getNumByCompanyId(String companyId);
    public int getNumByCompanyIdAndRecruitId(String companyId, String recruiterId);
    public int addDelivery(Deliveries deliveries);
    public int deleteDelivery(String userId,String companyId,String recruiterId);
    public String getRecruitId(String userId,String companyId);
    public Deliveries getMessage(String userId,String companyId,String recruitId);
    public List<Map<String,Object>> getCountByDate(String start, String end, String companyId);
    public List<Map<String,Object>> getCountByRecruitId(String companyId);
    public List<Deliveries> getAllBySendDate(Date sendDate,String companyId);
}
