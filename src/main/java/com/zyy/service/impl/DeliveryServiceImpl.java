package com.zyy.service.impl;

import com.zyy.dao.DeliveryMapper;
import com.zyy.dao.RecruitMapper;
import com.zyy.entity.Deliveries;
import com.zyy.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class DeliveryServiceImpl implements DeliveryService {

    @Autowired
    private DeliveryMapper deliveryMapper;

    @Autowired
    private RecruitMapper recruitMapper;

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

    @Override
    public List<Map<String, Object>> getCountByDate(String start, String end, String companyId) {
        Date startDate;
        Date endDate;
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startLocalDate=LocalDate.parse(start,formatter);
        LocalDate endLocalDate=LocalDate.parse(end,formatter);
        startDate= java.sql.Date.valueOf(startLocalDate);
        endDate= java.sql.Date.valueOf(endLocalDate);
        int total = deliveryMapper.getCountByStartAndEnd(startDate,endDate,companyId);
        List<Map<String, Object>> list=new ArrayList<>();
        while (startDate.before(endDate)){
            int count = deliveryMapper.getCountByDate(startDate,companyId);
            Map<String, Object> map=new HashMap<>();
            map.put("date",startDate);
            map.put("newCount",count);
            map.put("allCount",total);
            list.add(map);
            LocalDate newStartLocalDate = startLocalDate.plusDays(1);// 日期加一天
            startLocalDate = newStartLocalDate;
            startDate = java.sql.Date.valueOf(newStartLocalDate);
        }
        int count = deliveryMapper.getCountByDate(endDate,companyId);
        Map<String, Object> map=new HashMap<>();
        map.put("date",endDate);
        map.put("newCount",count);
        map.put("allCount",total);
        list.add(map);
        return list;
    }

    @Override
    public List<Map<String, Object>> getCountByRecruitId(String companyId) {
        List<String> careerList = recruitMapper.getCareerListByCompanyId(companyId);
        List<Map<String, Object>> list=new ArrayList<>();
        for (String career : careerList) {
            String recruitId = recruitMapper.getRecruitIdByCareer(career);
            int count = deliveryMapper.getCountByRecruitId(recruitId);
            Map<String, Object> map=new HashMap<>();
            map.put("name",career);
            map.put("value",count);
            list.add(map);
        }
        return list;
    }
}
