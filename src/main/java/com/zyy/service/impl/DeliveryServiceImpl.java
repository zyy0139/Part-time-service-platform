package com.zyy.service.impl;

import com.zyy.dao.DeliveryMapper;
import com.zyy.entity.Deliveries;
import com.zyy.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveryServiceImpl implements DeliveryService {

    @Autowired
    private DeliveryMapper deliveryMapper;


    @Override
    public List<Deliveries> getAllByCompanyId(String companyId) {
        List<Deliveries> deliveryList = deliveryMapper.getAllByCompanyId(companyId);
        return deliveryList;
    }

    @Override
    public List<Deliveries> getAllByCompanyIdAndRecruitId(String companyId, String recruiterId) {
        List<Deliveries> deliveryList = deliveryMapper.getAllByCompanyIdAndRecruitId(companyId, recruiterId);
        return deliveryList;
    }

    @Override
    public int getNumByCompanyId(String companyId) {
        int num=deliveryMapper.getNumByCompanyId(companyId);
        return num;
    }

    @Override
    public int getNumByCompanyIdAndRecruitId(String companyId, String recruiterId) {
        int num=deliveryMapper.getNumByCompanyIdAndRecruitId(companyId,recruiterId);
        return num;
    }

    @Override
    public int addDelivery(Deliveries deliveries) {
        int result=deliveryMapper.addDelivery(deliveries);
        return result;
    }

    @Override
    public int deleteDelivery(String userId, String companyId, String recruitId) {
        int result=deliveryMapper.deleteDelivery(userId,companyId,recruitId);
        return result;
    }

    @Override
    public String getRecruitId(String userId, String companyId) {
        String recruitId=deliveryMapper.getRecruitId(userId,companyId);
        return recruitId;
    }

    @Override
    public Deliveries getMessage(String userId, String companyId, String recruitId) {
        Deliveries delivery=deliveryMapper.getMessage(userId,companyId,recruitId);
        return delivery;
    }
}
