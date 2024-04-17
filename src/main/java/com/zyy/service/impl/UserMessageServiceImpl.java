package com.zyy.service.impl;

import com.zyy.dao.UserMessageMapper;
import com.zyy.entity.UserMessages;
import com.zyy.service.UserMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserMessageServiceImpl implements UserMessageService {

    @Autowired
    private UserMessageMapper userMessageMapper;

    @Override
    public int sendMessage(UserMessages userMessage) {
        return userMessageMapper.sendMessage(userMessage);
    }

    @Override
    public int deleteMessage(String messageId) {
        return userMessageMapper.deleteByMessageId(messageId);
    }

    @Override
    public int deleteMessageByUserId(String userId) {
        return userMessageMapper.deleteByUserId(userId);
    }

    @Override
    public List<UserMessages> getListByUserId(String userId) {
        return userMessageMapper.getListByUserId(userId);
    }

    @Override
    public int getCountByUserId(String userId) {
        return userMessageMapper.getCountByUserId(userId);
    }

    @Override
    public int getCountByUserIdAndIsRead(String userId) {
        return userMessageMapper.getCountByUserIdAndIsRead(userId);
    }

    @Override
    public int updateIsRead(String messageId) {
        return userMessageMapper.updateIsReadByMessageId(messageId);
    }
}
