package com.zyy.dao;

import com.zyy.entity.Users;

public interface UserMapper {
    public int userRegister(Users users);
    public int updateAllById(Users user);
    public int updatePasswordById(String id,String password);
    public Users selectAllById(String id);
    public String selectIdByEmail(String email);
    public String selectIdByAccount(String account);
    public String selectPasswordByAccount(String account);
    public Users selectAllByEmail(String email);
    public Users selectAllByEmailAndPassword(String email,String password);
    public Users selectAllByAccountAndPassword(String account,String password);
    public int updateIsAdmitById(String userId);
}
