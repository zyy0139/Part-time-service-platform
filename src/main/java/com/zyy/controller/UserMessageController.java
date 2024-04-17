package com.zyy.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.zyy.entity.UserMessages;
import com.zyy.service.impl.UserMessageServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.zyy.utils.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/zyy/userMessage")
@CrossOrigin(origins = "*")
public class UserMessageController {

    @Autowired
    private UserMessageServiceImpl userMessageService;

    @GetMapping("getUserMessageList")
    public Result getUserMessageList(HttpServletRequest request){ // 获取用户消息列表
        String header = request.getHeader("Authorization");
        String token = header.substring(18);
        DecodedJWT jwt = JWTUtils.verify(token);
        String userId = jwt.getSubject();
        List<UserMessages> userMessageList = userMessageService.getListByUserId(userId);
        List<Map<String,Object>> mapList = new ArrayList<>();
        for (UserMessages userMessage : userMessageList) {
            Map<String,Object> map = new HashMap<>();
            map.put("messageId",userMessage.getMessageId());
            map.put("userId",userMessage.getUserId());
            map.put("title",userMessage.getTitle());
            map.put("content", userMessage.getContent());
            map.put("sendDate",userMessage.getSendDate());
            map.put("isRead",userMessage.getIsRead());
            mapList.add(map);
        }
        int allCount = userMessageService.getCountByUserId(userId); // 全部消息数
        int noReadCount = userMessageService.getCountByUserIdAndIsRead(userId); // 未读消息数
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("allCount",allCount);
        resultMap.put("noReadCount",noReadCount);
        resultMap.put("messageList",mapList);
        return ResponseUtils.successResult("查询成功",resultMap);
    }

    @DeleteMapping("deleteByMessageId")
    public Result deleteByMessageId(@RequestParam String messageId){ // 删除单条消息
        int result = userMessageService.deleteMessage(messageId);
        if(result == 1){
            return ResponseUtils.successResult("删除成功");
        }else {
            return ResponseUtils.failResult("删除失败");
        }
    }

    @DeleteMapping("deleteReadMessage")
    public Result deleteReadMessage(HttpServletRequest request){ // 删除已读消息
        String header = request.getHeader("Authorization");
        String token = header.substring(18);
        DecodedJWT jwt = JWTUtils.verify(token);
        String userId = jwt.getSubject();
        int result = userMessageService.deleteMessageByUserId(userId);
        if(result == 1){
            return ResponseUtils.successResult("删除成功");
        }else {
            return ResponseUtils.failResult("删除失败");
        }
    }

}
