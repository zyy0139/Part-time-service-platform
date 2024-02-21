package com.zyy.dao;

import com.zyy.entity.Companies;

public interface CompanyMapper {
    public int companyRegister(Companies companies);
    public Companies selectAllByEmail(String email);
    public Companies selectAllByAccountAndPassword(String account,String password);
}
