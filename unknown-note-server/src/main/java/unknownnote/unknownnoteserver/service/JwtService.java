package unknownnote.unknownnoteserver.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {
    // private static final String SECRET_KEY = "ilovekwangwoonuniversity2019203094"; // 실제 사용시 충분히 복잡한 키 사용

    // properties 파일에 있는 secret key를 가져오는 것으로 수정
    @Value("${spring.jwt.secret}")
    private String SECRET_KEY; //비밀 키

    public Integer getUserIdFromJwt(String jwtToken) {
        jwtToken = jwtToken.replace(" ", "");
        jwtToken = jwtToken.replace("Bearer ", ""); // "Bearer " 접두사 제거
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

        // 토큰 파싱 및 검증
//        Jws<Claims> jwsClaims = Jwts.parserBuilder()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(jwtToken);
        // jjwt 0.12.3 버전 수정
        Jws<Claims> jwsClaims = Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwtToken);

        // 검증된 클레임 반환
        Claims claims = jwsClaims.getBody();
        Integer user_id = claims.get("user_id", Integer.class); // Integer 타입으로 user_id 추출
        return  user_id;
    }

}
