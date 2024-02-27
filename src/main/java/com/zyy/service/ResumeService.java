package com.zyy.service;

import com.zyy.entity.Resumes;

public interface ResumeService {
    public int addResume(Resumes resumes);
    public int updateResume(Resumes resumes);
    public Resumes getAllByUserId(String userId);
}
