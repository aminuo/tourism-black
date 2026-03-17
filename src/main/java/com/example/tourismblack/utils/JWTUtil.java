package com.example.tourismblack.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtil {
    // 密钥，用于签名token
    private static final String SECRET_KEY = "tourism_black_secret_key";
    // token过期时间，这里设置为7天
    private static final long EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000;

    /**
     * 生成token
     * @param openid 用户的openid
     * @return 生成的token
     */
    public static String generateToken(String openid) {
        // 创建claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("openid", openid);
        claims.put("created", new Date());

        // 生成token
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    /**
     * 从token中提取claims
     * @param token token字符串
     * @return claims
     */
    private static Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从token中提取openid
     * @param token token字符串
     * @return openid
     */
    public static String getOpenidFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims != null ? (String) claims.get("openid") : null;
    }

    /**
     * 验证token是否有效
     * @param token token字符串
     * @return 是否有效
     */
    public static boolean validateToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (claims == null) {
            return false;
        }
        // 检查token是否过期
        return claims.getExpiration().after(new Date());
    }
}