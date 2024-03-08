package unknownnote.unknownnoteserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unknownnote.unknownnoteserver.dto.UserInfoRequest;
import unknownnote.unknownnoteserver.dto.UserInfoResponse;
import unknownnote.unknownnoteserver.exception.UserNotFoundException;
import unknownnote.unknownnoteserver.service.ErrorService;
import unknownnote.unknownnoteserver.service.JwtService;
import unknownnote.unknownnoteserver.service.UserService;
import unknownnote.unknownnoteserver.service.UserSubscribeService;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Autowired
    private ErrorService errorService;

    @Autowired
    private UserSubscribeService userSubscribeService;

    @GetMapping
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String jwtToken) {
        try {
            int jwt_user_id = jwtService.getUserIdFromJwt(jwtToken); // JWT 토큰 검증

            try{
                UserInfoResponse response = userService.getUserInfo(jwt_user_id);
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.ok(errorService.setError(4000, "요청 처리를 실패했습니다"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(errorService.setError(2000,"유효하지 않은 접근입니다"));
        }
    }

    @PatchMapping
    public ResponseEntity<?> patchUserInfo(@RequestHeader("Authorization") String jwtToken, @RequestBody UserInfoRequest userInfoRequest){
        try {
            int jwt_user_id = jwtService.getUserIdFromJwt(jwtToken); // JWT 토큰 검증

            try{
                userService.patchUserInfo(jwt_user_id,userInfoRequest);
                return ResponseEntity.ok(errorService.setError(1000, ""));
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.ok(errorService.setError(4000, "요청 처리를 실패했습니다"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(errorService.setError(2000,"유효하지 않은 접근입니다"));
        }
    }

    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribe(@RequestHeader("Authorization") String jwtToken, @RequestParam int target_user_id) {
        try{
            int jwt_user_id = jwtService.getUserIdFromJwt(jwtToken); // JWT 토큰 검증

            try{
                userSubscribeService.subscribe(target_user_id, jwt_user_id);
                return ResponseEntity.ok(errorService.setError(1000, ""));
            }catch (Exception e){
                e.printStackTrace();
                return ResponseEntity.ok(errorService.setError(4000, "요청 처리를 실패했습니다"));
            }


        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.ok(errorService.setError(2000,"유효하지 않은 접근입니다"));
        }
    }

    @DeleteMapping("/subscribe")
    public ResponseEntity<?> unsubscribe(@RequestHeader("Authorization") String jwtToken, @RequestParam int target_user_id) {
        try{
            int jwt_user_id = jwtService.getUserIdFromJwt(jwtToken); // JWT 토큰 검증

            try{
                userSubscribeService.unsubscribe(target_user_id, jwt_user_id);
                return ResponseEntity.ok(errorService.setError(1000, ""));
            }catch (Exception e){
                e.printStackTrace();
                return ResponseEntity.ok(errorService.setError(4000, "요청 처리를 실패했습니다"));
            }


        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.ok(errorService.setError(2000,"유효하지 않은 접근입니다"));
        }
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.ok(errorService.setError(1001, "해당 ID의 유저가 없습니다"));
    }
}
