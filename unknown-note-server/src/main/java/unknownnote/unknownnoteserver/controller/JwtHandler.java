package unknownnote.unknownnoteserver.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@RequiredArgsConstructor
@Component
public class JwtHandler {

    @Value("${spring.jwt.secret}") // ******************************************중요**********************
    private String jwtSecret; //비밀 키

    public Jws<Claims> tokenValidation(String token) throws JwtException {
        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        try {
            //return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token); //토큰 검증
            // jjwt 0.12.3 버전 수정
            return Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseSignedClaims(token); //토큰 검증
        }catch(JwtException e){
            throw new IllegalStateException("JWT token validation failed : ", e);
        }
    }

    public int jwtDecoder(String token) {
        try {

            // JWT 토큰 해석하여 userid 추출
//            Claims claims = Jwts.parser()
//                    .setSigningKey(jwtSecret.getBytes())
//                    .parseClaimsJws(token)
//                    .getBody();
            // jjwt 0.12.3 버전 수정
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret.getBytes())
                    .build()
                    .parseSignedClaims(token)
                    .getBody();

            int userId = claims.get("userid", Integer.class); // 추출한 userid 값
            return userId;

        } catch (Exception e) {
            // 토큰 해석 중 예외가 발생
            //e.printStackTrace(); // 실제 운영에서는 로깅 등으로 대체할 예정
            throw new JwtException("Error during Decoding token :",e);
        }
    }

}
