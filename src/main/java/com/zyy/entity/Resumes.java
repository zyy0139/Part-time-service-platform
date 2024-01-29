package com.zyy.entity;

import lombok.Data;

/*
    简历信息类
 */

@Data
public class Resumes {
    private String id; //用户id
    private String career; //意向职业
    private String experience; //个人经历
}
