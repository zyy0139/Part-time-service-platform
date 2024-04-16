package com.zyy.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.zyy.entity.Deliveries;
import com.zyy.service.impl.CompanyServiceImpl;
import com.zyy.service.impl.DeliveryServiceImpl;
import com.zyy.service.impl.RecruitServiceImpl;
import com.zyy.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.zyy.utils.*;

import javax.servlet.http.HttpServletRequest;

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
        if(isDelivery == null){
            return ResponseUtils.failResult(ResultCode.exist_already,"您已投递过该岗位");
        }
        Deliveries delivery=new Deliveries();
        delivery.setUserId(userId);
        delivery.setCompanyId(companyId);
        delivery.setRecruitId(recruitId);
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
        int result1=deliveryService.deleteDelivery(userId,companyId,recruitId);
        if(result1==1){
            return ResponseUtils.successResult("驳回成功");
        }else {
            return ResponseUtils.failResult(ResultCode.delete_fail,"驳回失败");
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
            int result3 = deliveryService.deleteDelivery(userId,companyId,recruitId);
            if(result3==1){
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

}
