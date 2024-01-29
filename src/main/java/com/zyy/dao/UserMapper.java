package com.zyy.dao;

import com.zyy.entity.Users;

public interface UserMapper {
    public int userRegister(Users users);
    public int updateAllById(String id);
    public int selectAllById(String id);
}
