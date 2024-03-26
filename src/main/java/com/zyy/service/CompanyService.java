package com.zyy.service;

import com.zyy.entity.Companies;

import java.util.Map;

public interface CompanyService {
    public int companyRegister(Companies companies);
    public Companies selectAllByEmail(String email);
    public Map<String,Object> token(Companies companies);
    public int updateById(Companies companies);
    public int updatePasswordById(String password,String id);
    public Companies selectAllById(String id);
    public String selectIdByEmail(String email);
    public String getIdByName(String name);
}
