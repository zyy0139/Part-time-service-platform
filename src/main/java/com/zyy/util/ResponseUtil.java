package com.zyy.util;

public class ResponseUtil {
    public static Result successResult(){
        Result result=new Result();
        result.setCode(200);
        result.setMessage("成功！");
        return result;
    }

    public static Result successResult(String message){
        Result result=new Result();
        result.setCode(200);
        result.setMessage(message);
        return result;
    }

    public static Result successResult(int code,String message){
        Result result=new Result();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public static Result successResult(int code,String message,Object data){
        Result result=new Result();
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public static Result successResult(String message,Object data){
        Result result=new Result();
        result.setCode(200);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public static Result failResult(){
        Result result=new Result();
        result.setCode(500);
        result.setMessage("失败！");
        return result;
    }

    public static Result failResult(String message){
        Result result=new Result();
        result.setCode(500);
        result.setMessage(message);
        return result;
    }

    public static Result failResult(int code,String message){
        Result result=new Result();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public static Result failResult(String message,Object data){
        Result result=new Result();
        result.setCode(400);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public static Result failResult(int code,String message,Object data){
        Result result=new Result();
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }
}
