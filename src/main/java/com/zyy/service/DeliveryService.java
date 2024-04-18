package com.zyy.service;

import com.zyy.entity.Deliveries;

import java.util.List;
import java.util.Map;

public interface DeliveryService {
    public List<Deliveries> getAllByCompanyId(String companyId);
    public List<Deliveries> getAllByCompanyIdAndRecruitId(String companyId, String recruiterId);
    public int getNumByCompanyId(String companyId);
    public int getNumByCompanyIdAndRecruitId(String companyId, String recruiterId);
    public int addDelivery(Deliveries deliveries);
    public int deleteDelivery(String userId,String companyId,String recruiterId);
    public String getRecruitId(String userId,String companyId);
    public Deliveries getMessage(String userId,String companyId,String recruitId);
    public List<Map<String,Object>> getCountByDate(String start, String end, String companyId);
    public List<Map<String,Object>> getCountByRecruitId(String companyId);
}
