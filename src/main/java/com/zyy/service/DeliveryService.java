package com.zyy.service;

import com.zyy.entity.Deliveries;

import java.util.List;

public interface DeliveryService {
    public List<Deliveries> getAllByCompanyId(String companyId);
    public List<Deliveries> getAllByCompanyIdAndRecruitId(String companyId, String recruiterId);
    public int getNumByCompanyId(String companyId);
    public int getNumByCompanyIdAndRecruitId(String companyId, String recruiterId);
    public int addDelivery(Deliveries deliveries);
    public int deleteDelivery(String userId,String companyId,String recruiterId);
    public String getRecruitId(String userId,String companyId);
    public Deliveries getMessage(String userId,String companyId,String recruitId);
}
