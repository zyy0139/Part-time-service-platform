package com.zyy.handler;

import com.zyy.exception.BusinessException;
import com.zyy.util.ResponseUtil;
import com.zyy.util.Result;
import com.zyy.util.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/*
    异常处理
 */
@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {
    /**
     * 业务异常处理
     * @param e
     * @return ErrorInfo
     */
    @ExceptionHandler({BusinessException.class})
    public Result businessExceptionHandler(HttpServletRequest request, BusinessException e)
            throws BusinessException {
        log.error("BusinessException异常：{}", e.getMessage());
        return ResponseUtil.failResult(ResultCode.fail, e.getMessage(), null);
    }
}
