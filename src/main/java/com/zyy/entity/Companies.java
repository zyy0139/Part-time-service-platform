package com.zyy.entity;

import lombok.Data;

/*
    企业信息类
 */

@Data
public class Companies {
    private String id; //企业id
    private String account; //企业账号
    private String password; //企业密码
    private String email;
    private String name;
    private String address;
    private String phone;
}
