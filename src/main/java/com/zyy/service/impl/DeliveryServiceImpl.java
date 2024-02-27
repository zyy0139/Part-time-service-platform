package com.zyy.service.impl;

import com.zyy.dao.DeliveryMapper;
import com.zyy.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveryServiceImpl implements DeliveryService {

    @Autowired
    private DeliveryMapper deliveryMapper;

    @Override
    public List<String> getUserIdByCompanyId(String companyId) {
        List<String> userIdList=deliveryMapper.getUserIdByCompanyId(companyId);
        return userIdList;
    }

    @Override
    public int getNumByCompanyId(String companyId) {
        int num=deliveryMapper.getNumByCompanyId(companyId);
        return num;
    }
}
