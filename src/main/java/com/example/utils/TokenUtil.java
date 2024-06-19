package com.example.utils;

import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONUtil;
import com.example.exception.TestException;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Component
public class TokenUtil {

    public static long getCurrentId(HttpServletRequest request) {
        //获取头部header 信息
        try {
            String token = request.getHeader("token");
            System.out.printf("token:%s\n",token);
            Claims claims = JWTUtils.parseJWT(token);
            return Convert.toLong(claims.get("id"));
        } catch (Exception e) {
            throw new TestException("token解析失败");
        }
    }
}
