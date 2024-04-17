package com.zyy.entity;

import lombok.Data;
import java.util.Date;

/*
    学生消息类
 */
@Data
public class UserMessages {
    private String messageId; // 消息ID
    private String companyId; // 发送公司ID
    private String userId; // 接收学生ID
    private String title;// 标题
    private String content;// 内容
    private Date sendDate;// 发送时间
    private Boolean isRead;// 是否已读
}
