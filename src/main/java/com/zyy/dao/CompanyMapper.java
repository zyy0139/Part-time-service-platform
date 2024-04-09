package com.zyy.dao;

import com.zyy.entity.Companies;

import java.util.List;

public interface CompanyMapper {
    public int companyRegister(Companies companies);
    public Companies selectAllByEmail(String email);
    public Companies selectAllByAccountAndPassword(String account,String password);
    public int updateById(Companies companies);
    public int updatePasswordById(String password,String id);
    public Companies selectAllById(String id);
    public String selectIdByEmail(String email);
    public String getIdByName(String name);
    public String getNameById(String id);
    public List<String> selectIdByAddress(String address);
}
