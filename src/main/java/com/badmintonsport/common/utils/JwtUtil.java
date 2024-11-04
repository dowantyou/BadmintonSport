package com.badmintonsport.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

public class JwtUtil {
    /**
     * 生成jwt
     * 使用Hs256算法, 私匙使用固定秘钥
     *
     * @param secretKey jwt秘钥
     * @param ttlMillis jwt过期时间(毫秒)
     * @param claims    设置的信息
     * @return
     */
    public static String createJWT(String secretKey, long ttlMillis, Map<String, Object> claims) {
        // 指定签名的时候使用的签名算法，也就是header那部分
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        // 生成JWT的时间
        long expMillis = System.currentTimeMillis() + ttlMillis;
        Date exp = new Date(expMillis);

        // 设置jwt的body
        JwtBuilder builder = Jwts.builder()
                // 如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .setClaims(claims)
                // 设置签名使用的签名算法和签名使用的秘钥
                .signWith(signatureAlgorithm, secretKey.getBytes(StandardCharsets.UTF_8))
                // 设置过期时间
                .setExpiration(exp);

        return builder.compact();
    }

    /**
     * 创建 JSON Web Token（JWT）的方法。
     *
     * @param secretKey   用于签名 JWT 的密钥。
     * @param ttlMillis   JWT 的有效时间（以毫秒为单位）。
     * @param claims      包含在 JWT 中的声明（键值对）。
     * @return 生成的 JWT 字符串。
     */
//    public static String createJWT(String secretKey, long ttlMillis, Map<String, Object> claims) {
//        // 指定签名算法为 HS256，这是一种常用的对称加密算法。
//        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
//
//        // 计算 JWT 的过期时间，通过当前时间加上给定的有效时间得到。
//        long expMillis = System.currentTimeMillis() + ttlMillis;
//        Date exp = new Date(expMillis);
//
//        // 将密钥转换为字节数组，使用 UTF-8 编码。
//        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
//
//        // 使用 Jwts.builder() 创建 JWT 构建器。
//        return Jwts.builder()
//                // 设置自定义的声明到 JWT 的 payload 部分。
//                .setClaims(claims)
//                // 使用正确的方式进行签名，通过密钥字节数组创建 HMAC SHA 密钥并用于签名。
//                .signWith(Keys.hmacShaKeyFor(keyBytes))
//                // 设置 JWT 的过期时间。
//                .setExpiration(exp)
//                // 压缩生成最终的 JWT 字符串。
//                .compact();
//    }

    /**
     * Token解密
     *
     * @param secretKey jwt秘钥 此秘钥一定要保留好在服务端, 不能暴露出去, 否则sign就可以被伪造, 如果对接多个客户端建议改造成多个
     * @param token     加密后的token
     * @return
     */
    public static Claims parseJWT(String secretKey, String token) {
        // 得到DefaultJwtParser
        Claims claims = Jwts.parser()
                // 设置签名的秘钥
                .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                // 设置需要解析的jwt
                .parseClaimsJws(token).getBody();
        return claims;
    }

    /**
     * 解析 JWT 并获取声明。
     *
     * @param secretKey 用于验证 JWT 的密钥。
     * @param token     要解析的 JWT 字符串。
     * @return 包含在 JWT 中的声明。
     */
//    public static Claims parseJWT(String secretKey, String token) {
//            byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
//            return Jwts.parserBuilder()
//                    .setSigningKey(keyBytes)
//                    .build()
//                    .parseClaimsJws(token)
//                    .getBody();
//    }

}
