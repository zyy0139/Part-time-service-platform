package com.zyy.service.impl;

import com.zyy.dao.WorkTypeMapper;
import com.zyy.service.WorkTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkTypeServiceImpl implements WorkTypeService {

    @Autowired
    private WorkTypeMapper workTypeMapper;

    @Override
    public List<String> getType() {
        List<String> typeList=workTypeMapper.getType();
        return typeList;
    }
}
