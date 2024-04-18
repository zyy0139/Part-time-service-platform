package com.zyy.entity;

import lombok.Data;

import java.util.Date;

/*
    投递关系类
 */
@Data
public class Deliveries {
    private String userId;//学生用户Id
    private String companyId;//投递公司Id
    private String recruitId;//招聘岗位Id
    private Date sendDate; //投递时间
}
