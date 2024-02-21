package com.zyy.entity;

import lombok.Data;

/*
    用户信息类
 */

@Data
public class Users {
    private String id; //用户ID
    private String account; //用户账号
    private String password; //用户密码
    private String email; //用户邮箱
    private String name;
    private String sex;
    private String age;
    private String address;
    private String school;
    private String profession; //用户专业
    private String phone;
    private int isAdmit;
}
