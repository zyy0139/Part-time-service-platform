package com.zyy.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.zyy.entity.Deliveries;
import com.zyy.service.impl.DeliveryServiceImpl;
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

    @PostMapping("/addDelivery")
    public Result addDelivery(@RequestParam String companyId, HttpServletRequest request){
        String token=String.valueOf(request.getAttribute(JWTUtils.USER_TOKEN));
        if(token==null){
            return ResponseUtils.failResult("无法解析到token");
        }
        DecodedJWT jwt=JWTUtils.verify(token);
        String userId=jwt.getSubject();
        Deliveries delivery=new Deliveries();
        delivery.setUserId(userId);
        delivery.setCompanyId(companyId);
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

}
