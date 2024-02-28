package com.zyy.entity;

import lombok.Data;

/*
    企业招聘信息类
 */

@Data
public class Recruits {
    private String recruitId;//招聘信息Id
    private String companyId; //企业Id
    private String career; //招聘职业
    private String type;//招聘岗位类型
    private int number; //招聘人数
    private String message; //招聘要求
    private int salary; //薪资
    private boolean freefl; //是否包吃住
    private String inform; //联系方式
}
