package com.zyy.service;

import com.zyy.entity.Users;

public interface UserService {
    public int Regist(Users users);
    public int UpdateAllById(String id);
    public Users SelectAllById(String id);
}