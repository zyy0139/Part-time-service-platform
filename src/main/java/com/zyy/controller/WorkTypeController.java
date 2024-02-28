package com.zyy.controller;

import com.zyy.service.impl.WorkTypeServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.zyy.utils.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/zyy/workType")
@CrossOrigin(origins = "*")
public class WorkTypeController {

    @Autowired
    private WorkTypeServiceImpl workTypeService;

    @GetMapping("/getType")
    public Result getType(){
        List<String> typeList=workTypeService.getType();
        Map<String,Object> map=new HashMap<>();
        map.put("typeList",typeList);
        return ResponseUtils.successResult("查询成功",map);
    }

}
