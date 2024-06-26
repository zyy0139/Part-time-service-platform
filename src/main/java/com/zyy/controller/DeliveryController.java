package com.zyy.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.zyy.entity.Deliveries;
import com.zyy.entity.UserMessages;
import com.zyy.service.impl.*;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.zyy.utils.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/zyy/delivery")
@CrossOrigin(origins = "*")
public class DeliveryController {

    @Autowired
    private DeliveryServiceImpl deliveryService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private RecruitServiceImpl recruitService;

    @Autowired
    private CompanyServiceImpl companyService;

    @Autowired
    private UserMessageServiceImpl userMessageService;

    @Autowired
    private Redisson redisson;

    @PostMapping("/addDelivery")
    public Result addDelivery(@RequestParam String recruitId,@RequestParam String companyName, HttpServletRequest request){
        String header= request.getHeader("Authorization");
        if(header==null){
            return ResponseUtils.failResult("未检测到token");
        }
        String token=header.substring(18);
        DecodedJWT jwt=JWTUtils.verify(token);
        String userId=jwt.getSubject();
        String companyId=companyService.getIdByName(companyName);
        int number = recruitService.getNumber(recruitId);
        if(number==0){
            return ResponseUtils.failResult(ResultCode.add_fail,"该岗位已无空余名额");
        }
        Deliveries isDelivery = deliveryService.getMessage(userId,companyId,recruitId);
        if(isDelivery!=null){
            return ResponseUtils.failResult(ResultCode.exist_already,"您已投递过该岗位");
        }
        Deliveries delivery=new Deliveries();
        delivery.setUserId(userId);
        delivery.setCompanyId(companyId);
        delivery.setRecruitId(recruitId);
        Date nowDate = DateUtils.getNow();
        delivery.setSendDate(nowDate);
        int result=deliveryService.addDelivery(delivery);
        if(result==1){
            return ResponseUtils.successResult("投递成功");
        }else {
            return ResponseUtils.failResult("投递失败");
        }
    }

    @DeleteMapping("/passDelivery")
    public Result passDelivery(@RequestParam String userId,@RequestParam String recruitId,HttpServletRequest request){
        String header= request.getHeader("Authorization");
        if(header==null){
            return ResponseUtils.failResult("未检测到token");
        }
        String token=header.substring(18);
        DecodedJWT jwt=JWTUtils.verify(token);
        String companyId=jwt.getSubject();
        UserMessages userMessage = new UserMessages();
        String messageId = RadomUtils.creatId();
        String title = "简历未通过";
        String content = "很抱歉通知您，您所投递的"+companyService.getNameById(companyId)
                +"公司的"+recruitService.getCareerByRecruitId(recruitId)+"岗位未能通过简历筛选，请您重新投递。";
        Date newDate = DateUtils.getNow();
        userMessage.setMessageId(messageId);
        userMessage.setCompanyId(companyId);
        userMessage.setUserId(userId);
        userMessage.setTitle(title);
        userMessage.setContent(content);
        userMessage.setSendDate(newDate);
        userMessage.setIsRead(false);
        int result1=deliveryService.deleteDelivery(userId,companyId,recruitId);
        int result2= userMessageService.sendMessage(userMessage);
        if(result1==1 && result2==1){
            return ResponseUtils.successResult("驳回成功");
        } else if (result1 == 0) {
            return ResponseUtils.failResult(ResultCode.delete_fail,"驳回失败");
        }else {
            return ResponseUtils.failResult(ResultCode.add_fail,"发送消息失败");
        }
    }

    @DeleteMapping("/admitDelivery")
    public Result admitDelivery(@RequestParam String userId,@RequestParam String recruitId,HttpServletRequest request){
        String header= request.getHeader("Authorization");
        if(header==null){
            return ResponseUtils.failResult("未检测到token");
        }
        String token=header.substring(18);
        DecodedJWT jwt=JWTUtils.verify(token);
        String companyId=jwt.getSubject();
        // 防并发处理
        RLock redissonLock = redisson.getLock(companyId);
        redissonLock.lock(); // 加锁
        try {
            int number=recruitService.getNumber(recruitId);
            number -=1;
            int result1 = recruitService.updateNumber(recruitId,number);
            if(result1==0){
                return ResponseUtils.failResult(ResultCode.update_fail,"更新数据失败");
            }
            int result2 = userService.updateIsAdmitByUserId(userId);
            if(result2==0){
                return ResponseUtils.failResult(ResultCode.update_fail,"更新数据失败");
            }
            UserMessages userMessage = new UserMessages();
            String messageId = RadomUtils.creatId();
            String title = "简历已通过";
            String content = "恭喜您，您所投递的"+companyService.getNameById(companyId)
                    +"公司的"+recruitService.getCareerByRecruitId(recruitId)+"岗位已通过简历筛选，祝您的兼职生活愉快!";
            Date newDate = DateUtils.getNow();
            userMessage.setMessageId(messageId);
            userMessage.setCompanyId(companyId);
            userMessage.setUserId(userId);
            userMessage.setTitle(title);
            userMessage.setContent(content);
            userMessage.setSendDate(newDate);
            userMessage.setIsRead(false);
            int result3 = userMessageService.sendMessage(userMessage);
            if(result3==0){
                return ResponseUtils.failResult(ResultCode.add_fail,"发送消息失败");
            }
            int result4 = deliveryService.deleteDelivery(userId,companyId,recruitId);
            if(result4==1){
                return ResponseUtils.successResult("录用成功");
            }else {
                return ResponseUtils.failResult(ResultCode.delete_fail,"录用失败");
            }
        }catch (Exception e){
            log.error("admitDelivery error",e); // 打印日志
            return ResponseUtils.failResult("系统错误");
        }finally {
            redissonLock.unlock(); // 解锁
        }

    }

    @GetMapping("/getCountByDate")
    public Result getCountByDate(@RequestParam String start, @RequestParam String end, HttpServletRequest request){
        String header= request.getHeader("Authorization");
        String token=header.substring(18);
        DecodedJWT jwt=JWTUtils.verify(token);
        String companyId=jwt.getSubject();
        List<Map<String,Object>> list = deliveryService.getCountByDate(start,end,companyId);
        return ResponseUtils.successResult("查询成功",list);
    }

    @GetMapping("/getCountByType")
    public Result getCountByType(HttpServletRequest request){
        String header= request.getHeader("Authorization");
        String token=header.substring(18);
        DecodedJWT jwt=JWTUtils.verify(token);
        String companyId=jwt.getSubject();
        List<Map<String,Object>> list = deliveryService.getCountByRecruitId(companyId);
        return ResponseUtils.successResult("查询成功",list);
    }

}
