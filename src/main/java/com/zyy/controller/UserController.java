package com.zyy.controller;

import com.zyy.entity.Users;
import com.zyy.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/zyy/user")
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @RequestMapping("/register")
    public String Register(@RequestParam("account") String account,@RequestParam("password") String password,
                            @RequestParam("email") String email){
        String userId=userService.SelectIdByEmail(email);
        if (userId!=null){
            return "邮箱已注册";
        }
        Users user=new Users();
        Random random=new Random();
        String id=String.valueOf(random.nextInt(99999)+300000);
        user.setId(id);
        user.setAccount(account);
        user.setPassword(password);
        user.setEmail(email);
        int result=userService.Regist(user);
        if(result==1){
            return "注册成功";
        }else {
            return "注册失败";
        }
    }
}
