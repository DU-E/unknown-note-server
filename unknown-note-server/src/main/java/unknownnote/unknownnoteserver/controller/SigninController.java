package unknownnote.unknownnoteserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import unknownnote.unknownnoteserver.entity.UserEntity;
import unknownnote.unknownnoteserver.service.UserService;

import java.util.Map;


@RestController
@RequestMapping("/signin")
public class SigninController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> signin(@RequestBody Map<String, String> body) {
        String method = body.get("method");
        String accessToken = body.get("token");

        // method와 accessToken 검증
        if (method == null || accessToken == null) {
            return new ResponseEntity<>(Map.of("code", 4000, "message", "요청 처리를 실패했습니다"), HttpStatus.BAD_REQUEST);
        }

        try {
            UserEntity userEntity = userService.processLogin(method, accessToken);
            String jwtToken = userService.generateJwtToken(userEntity.getUserId());

            return new ResponseEntity<>(Map.of("code", 1000, "data", jwtToken), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("code", 4000, "message", "요청 처리를 실패했습니다"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
