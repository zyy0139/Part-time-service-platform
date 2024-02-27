package com.zyy.service;

import com.zyy.entity.Deliveries;

import java.util.List;

public interface DeliveryService {
    public List<String> getUserIdByCompanyId(String companyId);
    public int getNumByCompanyId(String companyId);
    public int addDelivery(Deliveries deliveries);
    public int deleteByUserIdAndCompanyId(String userId,String companyId);
}
