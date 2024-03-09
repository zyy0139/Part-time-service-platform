package com.zyy.dao;

import com.zyy.entity.Deliveries;

import java.util.List;

public interface DeliveryMapper {
    public List<String> getUserIdByCompanyId(String companyId);
    public int getNumByCompanyId(String companyId);
    public int addDelivery(Deliveries deliveries);
    public int deleteByUserIdAndCompanyId(String userId,String companyId);
    public String getRecruitId(String userId,String companyId);
}
