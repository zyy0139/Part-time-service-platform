package com.zyy.Interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.zyy.utils.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)  {

        //http的header中获得token
        String token = null;
        String token1=request.getHeader("Authorization");
        if(token1!=null){
            if (token1.length()>18) {
                try {
                    token = token1.substring(18);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                request.setAttribute("USER_TOKEN", token);
                //token不存在
                try {
                    if (token == null || token.equals("")){

                        response.setCharacterEncoding("UTF-8");
                        response.setContentType("application/json; charset=utf-8");
                        PrintWriter out = null;
                        JSONObject res = new JSONObject();
                        res.put("fail", ResultCode.fail_token);
                        res.put("message", "请携带token");
                        out = response.getWriter();
                        out.append(res.toString());
                        return false;
                    }
                    //验证token
                    String sub = JWTUtils.validateToken(token);
                    if (sub == null || sub.equals("")){
                        response.setCharacterEncoding("UTF-8");
                        response.setContentType("application/json; charset=utf-8");
                        PrintWriter out = null;
                        JSONObject res = new JSONObject();
                        res.put("code", ResultCode.fail_token);
                        res.put("message", "token错误");
                        out = response.getWriter();
                        out.append(res.toString());
                        return false;
                    }

                    //更新token有效时间 (如果需要更新其实就是产生一个新的token)
                    if (JWTUtils.isNeedUpdate(token)) {
                        String newToken = JWTUtils.createToken(sub);
                        //                Date date=JWTUtil.getDxp(newToken);
                        response.setHeader("Authorization",JWTUtils.USER_TOKEN+"="+newToken);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                resptoken(response);
                return false;
            }
            return true;
        }else{
            resptoken(response);
            return false;
        }
    }

    private void resptoken(HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        JSONObject res = new JSONObject();
        res.put("code", ResultCode.fail_token);
        res.put("message", "请输入验证信息");
        try {
            out = response.getWriter();
            out.append(res.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
