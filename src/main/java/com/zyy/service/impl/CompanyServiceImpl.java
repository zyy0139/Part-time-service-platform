package com.zyy.service.impl;

import com.zyy.dao.CompanyMapper;
import com.zyy.entity.Companies;
import com.zyy.service.CompanyService;
import com.zyy.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyMapper companyMapper;

    @Override
    public int companyRegister(Companies companies) {
        int result=companyMapper.companyRegister(companies);
        return result;
    }

    @Override
    public Companies selectAllByEmail(String email) {
        Companies company=companyMapper.selectAllByEmail(email);
        return company;
    }

    @Override
    public Map<String, Object> token(Companies companies) {
        String account=companies.getAccount();
        String email=companies.getEmail();
        String password=companies.getPassword();
        Companies company;
        if(password==null){
            company=companyMapper.selectAllByEmail(email);
        }else {
            company=companyMapper.selectAllByAccountAndPassword(account,password);
        }
        Map<String,Object> map=new HashMap<>();
        if(company==null){
            map.put("code","0");
            return map;
        }
        String id=company.getId();
        String token= JWTUtils.createToken(id);
        map=new HashMap<>();
        map.put("company",company);
        map.put("token",token);
        map.put("code","1");
        map.put("dxp",JWTUtils.getDxp(token));
        return map;
    }

    @Override
    public int updateById(Companies companies) {
        int result=companyMapper.updateById(companies);
        return result;
    }

    @Override
    public int updatePasswordById(String password, String id) {
        int result=companyMapper.updatePasswordById(password,id);
        return result;
    }

    @Override
    public Companies selectAllById(String id) {
        Companies company=companyMapper.selectAllById(id);
        return company;
    }

    @Override
    public String selectIdByEmail(String email) {
        String companyId=companyMapper.selectIdByEmail(email);
        return companyId;
    }

    @Override
    public String getIdByName(String name) {
        String companyId=companyMapper.getIdByName(name);
        return companyId;
    }
}
