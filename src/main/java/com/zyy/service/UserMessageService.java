package com.zyy.service;

import com.zyy.entity.UserMessages;

import java.util.List;

public interface UserMessageService {
    public int sendMessage(UserMessages userMessage);
    public int deleteMessage(String messageId);
    public int deleteMessageByUserId(String userId);
    public List<UserMessages> getListByUserId(String userId);
    public int getCountByUserId(String userId);
    public int getCountByUserIdAndIsRead(String userId);
}
