package com.zyy.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.zyy.entity.Deliveries;
import com.zyy.service.impl.DeliveryServiceImpl;
import com.zyy.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
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

    @PostMapping("/addDelivery")
    public Result addDelivery(@RequestParam String recruitId,@RequestParam String companyId, HttpServletRequest request){
        String token=String.valueOf(request.getAttribute(JWTUtils.USER_TOKEN));
        if(token==null){
            return ResponseUtils.failResult("无法解析到token");
        }
        DecodedJWT jwt=JWTUtils.verify(token);
        String userId=jwt.getSubject();
        Deliveries delivery=new Deliveries();
        delivery.setUserId(userId);
        delivery.setCompanyId(companyId);
        delivery.setRecruitId(recruitId);
        int result=deliveryService.addDelivery(delivery);
        if(result==1){
            return ResponseUtils.successResult("投递成功");
        }else {
            return ResponseUtils.failResult(ResultCode.add_fail,"投递失败");
        }
    }

    @DeleteMapping("/passDelivery")
    public Result passDelivery(@RequestParam String userId,HttpServletRequest request){
        String token=String.valueOf(request.getAttribute(JWTUtils.USER_TOKEN));
        if(token==null){
            return ResponseUtils.failResult("无法解析到token");
        }
        DecodedJWT jwt=JWTUtils.verify(token);
        String companyId=jwt.getSubject();
        int result=deliveryService.deleteByUserIdAndCompanyId(userId,companyId);
        if(result==1){
            return ResponseUtils.successResult("驳回成功");
        }else {
            return ResponseUtils.failResult(ResultCode.delete_fail,"驳回失败");
        }
    }

    @DeleteMapping("/admitDelivery")
    public Result admitDelivery(@RequestParam String userId,HttpServletRequest request){
        String token=String.valueOf(request.getAttribute(JWTUtils.USER_TOKEN));
        if(token==null){
            return ResponseUtils.failResult("无法解析到token");
        }
        DecodedJWT jwt=JWTUtils.verify(token);
        String companyId=jwt.getSubject();
        int result1=deliveryService.deleteByUserIdAndCompanyId(userId,companyId);
        int result2=userService.updateIsAdmitByUserId(userId);
        if(result1==1 && result2==1){
            return ResponseUtils.successResult("录用成功");
        }else if (result1!=1){
            return ResponseUtils.failResult(ResultCode.delete_fail,"删除信息失败");
        }else {
            return ResponseUtils.failResult(ResultCode.update_fail,"修改信息失败");
        }
    }

}
