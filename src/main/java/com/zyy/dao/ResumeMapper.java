package com.zyy.dao;

import com.zyy.entity.Resumes;

import java.util.List;

public interface ResumeMapper {
    public int addResume(Resumes resumes);
    public int updateResume(Resumes resumes);
    public Resumes getAllByUserId(String userId);
    public Resumes getAllByResumeId(String resumeId);
}
