package com.zyy.controller;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zyy.service.impl.CompanyServiceImpl;
import com.zyy.service.impl.UserServiceImpl;
import com.zyy.utils.JWTUtils;
import com.zyy.utils.MD5Utils;
import com.zyy.utils.ResponseUtils;
import com.zyy.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.zyy.utils.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/zyy/password")
@CrossOrigin(origins = "*")
public class ResetPasswordController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private CompanyServiceImpl companyService;

    @Autowired
    private RedisUtils redisUtil;

    @PostMapping("/resetPassword")
    public Result resetPassword(@RequestBody String body) {
        Map<String, Object> map = JSON.parseObject(body, Map.class);
        String email = String.valueOf(map.get("email"));
        String userId = userService.SelectIdByEmail(email);
        String companyId =companyService.selectIdByEmail(email);
        String newPassword= String.valueOf(map.get("password"));
        String md5Password = MD5Utils.getMD5(newPassword);
        String emailCode = String.valueOf(map.get("emailCode"));
        Object emailKey = redisUtil.get(email);
        String id;
        int result;
        if (emailKey == null || !emailKey.toString().equals(emailCode)) {
            return ResponseUtils.failResult("验证码错误");
        }
        if(userId == null && companyId == null){
            return ResponseUtils.failResult(ResultCode.USER_EXIST,"用户不存在");
        }else if(userId!= null){
            id = userId;
            result=userService.UpdatePasswordById(id, md5Password);
        }else{
            id = companyId;
            result= companyService.updatePasswordById(id,md5Password);
        }
        if(result == 1){
            redisUtil.del(email);
            return ResponseUtils.successResult("密码修改成功");
        }else{
            return ResponseUtils.failResult("密码修改失败");
        }
    }
}
