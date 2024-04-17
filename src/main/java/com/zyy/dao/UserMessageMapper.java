package com.zyy.dao;

import com.zyy.entity.UserMessages;
import java.util.List;

public interface UserMessageMapper {
    public int sendMessage(UserMessages userMessage);
    public int deleteByMessageId(String messageId);
    public int deleteByUserId(String userID);// 删除该用户已读的消息
    public List<UserMessages> getListByUserId(String userId);
    public int getCountByUserId(String userId); // 获取该用户所有消息的数量
    public int getCountByUserIdAndIsRead(String userId); // 获取该用户未读消息的数量
    public int updateIsReadByMessageId(String messageId);
}
