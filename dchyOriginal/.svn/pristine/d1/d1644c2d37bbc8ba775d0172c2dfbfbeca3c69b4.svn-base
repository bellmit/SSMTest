package cn.gtmap.msurveyplat.portalol.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2020/12/5
 * @description TODO 获取时效性TOKEN
 */
@SuppressWarnings("all")
@Service
public class JwtUtil {
    //私钥
    final static String base64EncodedSecretKey = "admin";
    //过期时间,十分钟
    final static long TOKEN_EXP = 1000 * 60 * 5;

    public static String getToken(String userName) {
        return Jwts.builder()
                .setSubject(userName)
                .claim("roles", "user")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXP))
                .signWith(SignatureAlgorithm.HS256, base64EncodedSecretKey)
                .compact();
    }

    /**
     * 检查token,只要不正确就会抛出异常
     **/
    public static boolean checkToken(String token) {
        try {
            final Claims claims = Jwts.parser().setSigningKey(base64EncodedSecretKey).parseClaimsJws(token).getBody();
            //return claims;
            return true;
        } catch (ExpiredJwtException e1) {
            //throw new Exception("登录信息过期，请重新登录");
            return false;
        } catch (Exception e) {
            //throw new Exception("用户未登录，请重新登录");
            return false;
        }
    }

//    public static void main(String[] args) {
//        String Token = getToken("liuqiang");
////        String Token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsaXVxaWFuZyIsInJvbGVzIjoidXNlciIsImlhdCI6MTYwNzEzODY2MiwiZXhwIjoxNjA3MTM4NzIyfQ.DjiHrfUovgYoLUv-mAcIpvIoJcNRGQl7lwH4MmcEk2U";
//        boolean flag = checkToken(Token);
//        System.out.println(Token);
//        System.out.println(flag);
//    }
}
