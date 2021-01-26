package www.pactera.com.coverage.project.AccessControl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;


public class JwtUtils {


    /**
     * 生成token令牌
     * @param subject
     * @return
     */
    public static String createToken(String subject){
        Date nowDate = new Date();
        // 过期时间
        Date expireDate = new Date(System.currentTimeMillis() + 60 * 60 * 24 * 1000);

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512,"MyJwtSecret")
                .compact();
    }

    /**
     * 从令牌获取注册信息
     * @param token
     * @return
     */
    public static Claims getTokenClaim(String token){
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey("MyJwtSecret")
                    .parseClaimsJws(token.replace("Bearer ", "")).getBody();
            return  claims;
        }catch (Exception e){
            e.printStackTrace();
            return  null;
        }

    }


    /**
     * 获取令牌中的用户名
     * @param token
     * @return
     */
    public static String getUsernameFromToken(String token){
        try{
            String username = Jwts.parser()
                    .setSigningKey("MyJwtSecret")
                    .parseClaimsJws(token.replace("Bearer ", ""))
                    .getBody()
                    .getSubject();
            return username;
        }catch (Exception e){
            //e.printStackTrace();
            return null;
        }

    }

    /**
     * 判断令牌是否过期
     * @param token
     * @return
     */
    public static boolean isTokenDateExpired (String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }

    /**
     * 获取token的过期时间
     * @param token
     * @return
     */
    public static Date getExpirationDateFromToken(String token){
        return  getTokenClaim(token).getExpiration();
    }

}
