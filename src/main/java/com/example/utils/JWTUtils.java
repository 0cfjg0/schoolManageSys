package com.example.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

public class JWTUtils {
    private static String signKey = "cfjg";
//    private static Long expire = 43200000L;

    public static String generateJwt(Map<String,Object> claims){//生成令牌
        String jwt = Jwts.builder().addClaims(claims)
                //添加数据
                .signWith(SignatureAlgorithm.HS256,signKey)
                //设置算法
                .setExpiration(new Date(System.currentTimeMillis() + 1000L*120*60))
                //设置过期时间2h
                .compact();
                //生成
        return jwt;
    }

    public static Claims parseJWT(String jwt){
        Claims claims = Jwts.parser()
                .setSigningKey(signKey)
                //设置秘钥
                .parseClaimsJws(jwt)
                //解析令牌
                .getBody();
                //获取数据
        return claims;
    }
}
