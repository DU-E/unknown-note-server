package unknownnote.unknownnoteserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unknownnote.unknownnoteserver.dto.MyProfileResponse;
import unknownnote.unknownnoteserver.dto.UserInfoResponse;
import unknownnote.unknownnoteserver.service.*;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private MyProfileService myProfileService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ErrorService errorService;

    @GetMapping
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String jwtToken, @RequestParam("user_id") int param_user_id) {
        try {
            int jwt_user_id = jwtService.getUserIdFromJwt(jwtToken); // JWT 토큰 검증
            boolean meWatchingMyProfile;

            if(jwt_user_id == param_user_id) { // 내 프로필
                try{
                    meWatchingMyProfile = true;
                    MyProfileResponse response = myProfileService.getMyProfileInfo(jwt_user_id,jwt_user_id,meWatchingMyProfile);
                    return ResponseEntity.ok(response);
                } catch (Exception e ){
                    e.printStackTrace();
                    return ResponseEntity.ok(errorService.setError(4000, "요청 처리를 실패했습니다"));
                }
            }
            else {  // 다른 사람 프로필
                try{
                    meWatchingMyProfile = false;
                    MyProfileResponse response = myProfileService.getMyProfileInfo(jwt_user_id, param_user_id, meWatchingMyProfile);
                    return ResponseEntity.ok(response);
                } catch (Exception e ){
                    e.printStackTrace();
                    return ResponseEntity.ok(errorService.setError(4000, "요청 처리를 실패했습니다"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(errorService.setError(2000,"유효하지 않은 접근입니다"));
        }
    }
}
