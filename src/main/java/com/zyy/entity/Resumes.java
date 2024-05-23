package com.zyy.entity;

import lombok.Data;

/*
    简历信息类
 */

@Data
public class Resumes {
    private String resumeId; //简历Id
    private String userId; //用户Id
    private String career; //意向职业
    private String type;//意向岗位类型
    private String skill;//技能
    private String experience; //个人经历
}
