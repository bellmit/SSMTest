package cn.gtmap.msurveyplat.portalol.utils.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.*;

/**
 * @author <a href="mailto:liuqiang@gtmap.cn">liuqiang</a>
 * @version 1.0, 2020/12/5
 * @description TODO
 */
public class JwtTokenUtil {

    // 过期时间5分钟
    private static final String SECRET = "南京国图Public-Api";
    private static final String Md5Key = "random-key";
    // 过期时间30分钟
    private static final long EXPIRE_TIME = 5 * 60 * 1000;

    /**
     * 获取用户名从token中
     */
    public static String getCodeFromToken(String token) {
        return getClaimFromToken(token).getSubject();
    }

    /**
     * 获取jwt的payload部分
     */
    public static Claims getClaimFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(Base64.getEncoder().encode(SECRET.getBytes()))
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 获取jwt失效时间
     */
    public static Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token).getExpiration();
    }

    /**
     * <pre>
     *  验证token是否失效
     *  true:过期   false:没过期
     * </pre>
     */
    public static Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.after(new Date());
    }

    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 生成token(通过用户名和签名时候用的随机数)
     */
    public static String generateToken(String userName, String randomKey) {
        Map<String, Object> claims = new HashMap();
        claims.put(Md5Key, randomKey);
        return doGenerateToken(claims, userName);
    }

    /**
     * 生成token
     */
    private static String doGenerateToken(Map<String, Object> claims, String subject) {
        final Date createdDate = new Date();
        final Date expirationDate = new Date(createdDate.getTime() + EXPIRE_TIME);
        String compactJws = Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, Base64.getEncoder().encode(SECRET.getBytes()))
                .compact();
        return compactJws;
    }
}
