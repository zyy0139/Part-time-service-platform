package com.zyy.service.impl;

import com.zyy.dao.ResumeMapper;
import com.zyy.entity.Resumes;
import com.zyy.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResumeServiceImpl implements ResumeService {

    @Autowired
    private ResumeMapper resumeMapper;

    @Override
    public int addResume(Resumes resumes) {
        int result=resumeMapper.addResume(resumes);
        return result;
    }

    @Override
    public int updateResume(Resumes resumes) {
        int result=resumeMapper.updateResume(resumes);
        return result;
    }

    @Override
    public Resumes getAllByUserId(String userId) {
        Resumes resume=resumeMapper.getAllByUserId(userId);
        return resume;
    }

    @Override
    public Resumes getAllByResumeId(String resumeId) {
        return resumeMapper.getAllByResumeId(resumeId);
    }
}
