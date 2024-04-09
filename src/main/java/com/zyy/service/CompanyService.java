package com.zyy.service;

import com.github.pagehelper.PageInfo;
import com.zyy.entity.Companies;

import java.util.List;
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
    public String getNameById(String id);
    public PageInfo<Companies> selectAll(int page, int pageSize);
    public int selectAllNum();
}
