package com.zyy.service.impl;

import com.zyy.dao.UserMapper;
import com.zyy.entity.Users;
import com.zyy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public int Regist(Users users) {
        int result=userMapper.userRegister(users);
        return result;
    }

    @Override
    public int UpdateAllById(String id) {
        int result=userMapper.updateAllById(id);
        return result;
    }

    @Override
    public Users SelectAllById(String id) {
        Users users=userMapper.selectAllById(id);
        return users;
    }
}
