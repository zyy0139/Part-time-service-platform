package com.zyy.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@ConfigurationProperties(prefix = "jwt")
public class JWTUtils {

    private static final Logger logger = LoggerFactory.getLogger(JWTUtils.class);
    //定义token返回头部
    public static String header;

    //token前缀
    public static String tokenPrefix;

    //签名密钥
    public static String secret;

    //有效期
    public static long expireTime;

    //存进客户端的token的key名
    public static final String USER_TOKEN = "USER_TOKEN";

    public void setHeader(String header) {
        JWTUtils.header = header;
    }

    public void setTokenPrefix(String tokenPrefix) {
        JWTUtils.tokenPrefix = tokenPrefix;
    }

    public void setSecret(String secret) {
        JWTUtils.secret = secret;
    }

    public void setExpireTime(int expireTimeInt) {

        JWTUtils.expireTime = expireTimeInt * 60 * 1000;

    }

    /**
     * 创建TOKEN
     * @param sub
     * @return
     */
    public static String createToken(String sub){
        Algorithm algorithm=Algorithm.HMAC256(secret);
        Date date=new Date(System.currentTimeMillis() + expireTime);
        return tokenPrefix + JWT.create()
                .withSubject(sub)
                .withExpiresAt(date)
                .sign(algorithm);
    }
    /**
     * 验证token
     * @param token
     */
    public static String validateToken(String token) throws Exception {
        try {
            return JWT.require(Algorithm.HMAC256(secret))
                    .build()
                    .verify(token.replace(tokenPrefix, ""))
                    .getSubject();
        } catch (TokenExpiredException e){
//            logger.info("token已经过期");
            return null;
        } catch (Exception e){
            logger.info("token验证失败");
            return null;
        }
    }

    public static DecodedJWT verify(String token){
        return JWT.require(Algorithm.HMAC256(secret)).build().verify(token);
    }
    /**
     * 检查token是否需要更新
     * @param token
     * @return
     */
    public static boolean isNeedUpdate(String token) throws Exception {
        //获取token过期时间
        Date expiresAt = null;
        try {
            expiresAt = JWT.require(Algorithm.HMAC512(secret))
                    .build()
                    .verify(token.replace(tokenPrefix, ""))
                    .getExpiresAt();
        } catch (TokenExpiredException e){
            return true;
        } catch (Exception e){
            throw new Exception("token验证失败");
        }
        //如果剩余过期时间少于过期时常的一般时 需要更新
        return (expiresAt.getTime()-System.currentTimeMillis()) < (expireTime>>1);
    }

    public static Date getDxp(String token){

        Date expiresAt = JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token.replace(tokenPrefix, ""))
                .getExpiresAt();
        return new Date(expiresAt.getTime());
    }

}
