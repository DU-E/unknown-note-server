package unknownnote.unknownnoteserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unknownnote.unknownnoteserver.dto.CalenderInfo;
import unknownnote.unknownnoteserver.dto.CalenderResponse;
import unknownnote.unknownnoteserver.service.DiaryService;
import unknownnote.unknownnoteserver.service.ErrorService;
import unknownnote.unknownnoteserver.service.JwtService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cal")
public class CalenderController {

    @Autowired
    private DiaryService diaryService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ErrorService errorService;

    @GetMapping
    public ResponseEntity<?> getDiaryCalender(@RequestHeader("Authorization") String jwtToken, @RequestParam("year") int year, @RequestParam("month") int month) {
        try {
            int jwt_user_id = jwtService.getUserIdFromJwt(jwtToken); // JWT 토큰 검증

            try{
                List<CalenderInfo> diaries = diaryService.getDiariesForMonth(jwt_user_id, year, month);
                CalenderResponse response = new CalenderResponse(1000,"", diaries);
                return ResponseEntity.ok(response);
            } catch (Exception e){
                e.printStackTrace();
                return ResponseEntity.ok(errorService.setError(4000,"요청 처리를 실패했습니다"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(errorService.setError(2000,"유효하지 않은 접근입니다"));
        }
    }
}
