package com.zyy.service;

import com.zyy.entity.Companies;

public interface CompanyService {
    public int companyRegister(Companies companies);
    public Companies selectAllByEmail(String email);
}
