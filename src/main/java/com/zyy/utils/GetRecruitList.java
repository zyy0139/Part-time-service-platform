package com.zyy.utils;

import com.github.pagehelper.PageInfo;
import com.zyy.entity.Companies;
import com.zyy.entity.Recruits;
import com.zyy.service.impl.CompanyServiceImpl;
import com.zyy.service.impl.RecruitServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GetRecruitList {

    @Autowired
    private RecruitServiceImpl recruitService;

    @Autowired
    private CompanyServiceImpl companyService;

    public Map<String,Object> getList(PageInfo<Recruits> list,String companyId){
        List<Map<String,Object>> mapList=new ArrayList<>();
        List<Recruits> recruitsList=list.getList();
        for(int i=0;i<list.getSize();i++){
            Map<String,Object> map=new HashMap<>();
            Companies company=companyService.selectAllById(recruitsList.get(i).getCompanyId());
            map.put("recruitId",recruitsList.get(i).getRecruitId());
            map.put("companyName",company.getName());
            map.put("companyAddress",company.getAddress());
            map.put("companyEmail",company.getEmail());
            map.put("companyPhone",company.getPhone());
            map.put("career",recruitsList.get(i).getCareer());
            map.put("type",recruitsList.get(i).getType());
            map.put("number",recruitsList.get(i).getNumber());
            map.put("message",recruitsList.get(i).getMessage());
            map.put("salary",recruitsList.get(i).getSalary());
            map.put("freefl",recruitsList.get(i).isFreefl());
            map.put("releaseDate",recruitsList.get(i).getReleaseDate());
            mapList.add(map);
        }
        int total=recruitService.getNumByCompanyId(companyId);
        Map<String,Object> map1=new HashMap<>();
        map1.put("total",total);
        map1.put("recruitList",mapList);
        return map1;
    }
}
