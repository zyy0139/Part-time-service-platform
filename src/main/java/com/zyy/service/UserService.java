package com.zyy.service;

import com.zyy.entity.Users;

import java.util.Map;

public interface UserService {
    public int Regist(Users users);
    public int UpdateAllById(Users users);
    public Users SelectAllById(String id);
    public String SelectIdByEmail(String email);
    public String SelectIdByAccount(String account);
    public String SelectPasswordByAccount(String account);
    public Map<String,Object> token(Users users);
    public Users SelectAllByEmail(String email);
}
