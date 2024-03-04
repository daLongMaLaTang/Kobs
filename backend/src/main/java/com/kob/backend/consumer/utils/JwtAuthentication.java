package com.kob.backend.consumer.utils;


import com.kob.backend.utils.JwtUtil;
import io.jsonwebtoken.Claims;

public class JwtAuthentication {

    public static  Integer getUserId(String token) {
        int userId = -1;

        try {
            Claims claims = JwtUtil.parseJWT(token);  // 解析JWT，获取JWT的载荷
            userId = Integer.parseInt(claims.getSubject());  // 从载荷中获取"subject"，这个"subject"应该是用户ID
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return userId;
    }
    //匹配
}


