package com.zyy.service.impl;

import com.zyy.dao.UserMapper;
import com.zyy.entity.Users;
import com.zyy.service.UserService;
import com.zyy.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public int Regist(Users users) {
        int result=userMapper.userRegister(users);
        return result;
    }

    @Override
    public int UpdateAllById(Users users) {
        int result=userMapper.updateAllById(users);
        return result;
    }

    @Override
    public Users SelectAllById(String id) {
        Users users=userMapper.selectAllById(id);
        return users;
    }

    @Override
    public String SelectIdByEmail(String email) {
        String id=userMapper.selectIdByEmail(email);
        return id;
    }

    @Override
    public String SelectIdByAccount(String account) {
        String id=userMapper.selectIdByAccount(account);
        return id;
    }

    @Override
    public String SelectPasswordByAccount(String account) {
        String password=userMapper.selectPasswordByAccount(account);
        return password;
    }

    @Override
    public Map<String, Object> token(Users users) {
        String account=users.getAccount();
        String email=users.getEmail();
        String password=users.getPassword();
        Users user;
        if(password==null){
            user=userMapper.selectAllByEmail(email);
        }else{
            user=userMapper.selectAllByAccountAndPassword(account,password);
        }
        Map<String,Object> map=new HashMap<>();
        if(user==null){
            map.put("code","0");
            return map;
        }
        //将id注入token
        String id=user.getId();
        String token= JWTUtils.createToken(id);
        map=new HashMap<>();
        map.put("user",user);
        map.put("token",token);
        map.put("code","1");
        map.put("dxp",JWTUtils.getDxp(token));
        return map;
    }

    @Override
    public Users SelectAllByEmail(String email) {
        Users users=userMapper.selectAllByEmail(email);
        return users;
    }
}
