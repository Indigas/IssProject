package sk.durovic.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {
    private Map<String, Object> header = new HashMap<>();
    private Map<String, Object> payload = new HashMap<>();
    private Date getNow;
    private final String SECRET = "secret";   // must be at least 32 bytes - 32 chars
    private final Algorithm algorithm = Algorithm.HMAC256(SECRET);

    public static String createJWTtoken(String username){
        JwtUtil jwtUtil = new JwtUtil();
        jwtUtil.getNow = new Date();

         return JWT.create().withHeader(jwtUtil.header)
                .withClaim("user", username)
                .withIssuedAt(jwtUtil.getNow)
                .withExpiresAt(jwtUtil.getExpiration(jwtUtil.getNow))
                .sign(jwtUtil.algorithm);
    }

    private void createHeader(){
        header.put("alg", "HMAC256");
    }

    private void createPayload(){
    }

    private Date getExpiration(Date now){
        long expire = now.getTime() + (7*24*60*60*1000);
        return new Date(expire);
    }

}
