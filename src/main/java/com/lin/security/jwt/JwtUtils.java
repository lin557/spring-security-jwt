package com.lin.security.jwt;

import java.util.Calendar;
import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;

/**
 * JWT工具类
 * 
 * @author zhen.lin
 * @date 2023年2月4日
 */
public class JwtUtils {
    
    /**
     * 盐值 目前不用
     */
    private static String JWT_SECRET_SALT = "";

    /**
    签发对象：这个用户的id
    签发时间：现在
    有效时间：30分钟
    载荷内容：暂时设计为：这个人的名字，这个人的昵称
    加密密钥：这个人的id加上一串字符串
    */
   public static String createToken(String sessionId, String secret, String role, int expireMin) {

       Calendar nowTime = Calendar.getInstance();
       nowTime.add(Calendar.MINUTE, expireMin);
       Date expiresDate = nowTime.getTime();
       
       return JWT.create()
               // 签发对象
               .withAudience(sessionId)
               // 发行时间
               .withIssuedAt(new Date())
               // 有效时间
               .withExpiresAt(expiresDate)
               // 载荷，随便写几个都可以
               .withClaim("role", role)
               // 加密
               .sign(Algorithm.HMAC256(secret + JWT_SECRET_SALT));   
   }
   
   /**
    * 检验合法性，其中secret参数就应该传入的是用户的id
    * @param token
    * @throws TokenUnavailable
    */
   public static boolean verifyToken(String token, String secret) {
       try {
           JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret + JWT_SECRET_SALT)).build();
           verifier.verify(token);
           return true;
       } catch (Exception e) {
           //效验失败
           return false;
       }
   }
   
   /**
    * 获取签发对象
    */
    public static String getAudience(String token) {
        String audience = null;
        try {
            audience = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException j) {
            // no code
        }
        return audience;
    }
   
   /**
    * 通过载荷名字获取载荷的值
    */
    public static Claim getClaimByName(String token, String name){
        return JWT.decode(token).getClaim(name);
    }
}
