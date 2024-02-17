package com.zyy.service.impl;

import com.zyy.dao.CompanyMapper;
import com.zyy.entity.Companies;
import com.zyy.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
