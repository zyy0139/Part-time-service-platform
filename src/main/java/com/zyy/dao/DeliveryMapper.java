package com.zyy.dao;

import java.util.List;

public interface DeliveryMapper {
    public List<String> getUserIdByCompanyId(String companyId);
    public int getNumByCompanyId(String companyId);
}
