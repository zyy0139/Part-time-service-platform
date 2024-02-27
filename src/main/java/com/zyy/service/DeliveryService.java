package com.zyy.service;

import java.util.List;

public interface DeliveryService {
    public List<String> getUserIdByCompanyId(String companyId);
    public int getNumByCompanyId(String companyId);
}
