package unknownnote.unknownnoteserver.controller;

import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unknownnote.unknownnoteserver.dto.DiaryDTO;
import unknownnote.unknownnoteserver.entity.Diary;
import unknownnote.unknownnoteserver.service.DiaryService;
import unknownnote.unknownnoteserver.service.JwtService;
import unknownnote.unknownnoteserver.service.ErrorService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/diary")
public class DiaryController {

    private final DiaryService diaryService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ErrorService errorService;

    @Autowired
    private JwtHandler jwtHandler;
    private static final Logger logger = LoggerFactory.getLogger(DiaryController.class);

    @Autowired
    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    @PostMapping
    public ResponseEntity<Object> saveDiaryEntry(@RequestBody DiaryDTO diaryDTO, @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken) {
        try {
            logger.debug("Received diaryDTO: {}", diaryDTO); // 로깅 추가
            logger.debug("saveDiaryEntry method is called!");

            String token;
            if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
                token = jwtToken.replace("Bearer ", "");
            } else {
                token=jwtToken;
            }

            //jwtHandler.tokenValidation(token); //예외 1 가능
            int userId=jwtService.getUserIdFromJwt(jwtToken); //jwtHandler.jwtDecoder(token); //예외 2 가능


            Diary savedDiary=diaryService.SaveNewDiary(diaryDTO,userId);
            if(savedDiary!=null) {
                // Diary가 성공적으로 저장되었을 때의 응답
                Map<String, Object> response = new HashMap<>();
                response.put("code", 1000);
                response.put("message", "일기 저장 완료");
                return ResponseEntity.ok().body(response); // 이제 JSON 객체로 반환 (추가부분)
            }else{
                // 사용자를 찾을 수 없거나 저장에 실패한 경우의 응답
                return ResponseEntity.ok(errorService.setError(1002,"일기 저장 실패"));
            }
        }catch(IllegalStateException e){
            logger.error("jwtToken is not in proper form / Outdated", e); // 예외 발생 시 로깅
            return ResponseEntity.ok(errorService.setError(2000,"jwt 토큰 형식 오류"));
        }catch(JwtException e){
            logger.error("Error during Decoding Token", e); // 예외 발생 시 로깅
            return ResponseEntity.ok(errorService.setError(2000,"토큰 해석중 오류 발생"));
        } catch(RuntimeException e){
            logger.error("Unexpected Error during SaveNewDiary()", e); // 예외 발생 시 로깅
            return ResponseEntity.ok(errorService.setError(2000,"서비스 일기저장 함수에서 예상치 못한 에러발생"));
        }catch(Exception e){
            //e.printStackTrace(); // 운영시는 가릴것
            logger.error("Unexpected error during saving diary", e); // 예외 발생 시 로깅
            return ResponseEntity.ok(errorService.setError(4000,"일기 저장 중 예상치 못한 에러 발생"));
        }
    }

    @PatchMapping
    public ResponseEntity<Object> changeDiary(@RequestBody Map<String, Object> requestBody, @RequestHeader("Authorization") String jwtToken){
        try {
            int openable = (int) requestBody.get("openable");
            String dcontent = (String) requestBody.get("dcontent");
            int diaryid = (Integer) requestBody.get("diaryid");

            String token;
            if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
                token = jwtToken.replace("Bearer ", "");
            } else {
                token = jwtToken;
            }

            //jwtHandler.tokenValidation(token); //예외 1 가능
            int userid = jwtService.getUserIdFromJwt(jwtToken); //jwtHandler.jwtDecoder(token); //예외 2 가능


            Diary changedDiary = diaryService.updateDiary(diaryid,dcontent,openable,userid);
            if(changedDiary!=null){
                //update 성공
                Map<String, Object> response = new HashMap<>();
                response.put("code", 1000);
                response.put("message", "일기 패치 완료");
                return ResponseEntity.ok().body(response); // 이제 JSON 객체로 반환 (추가부분)
            }else{
                //update 실패
                return ResponseEntity.ok(errorService.setError(1002,"일기 업데이트 실패"));
            }
        }catch(IllegalStateException e){
            logger.error("jwtToken is not in proper form / Outdated", e); // 예외 발생 시 로깅
            return ResponseEntity.ok(errorService.setError(2000,"jwt 토큰 형식 오류"));
        }catch(JwtException e){
            logger.error("Error during Decoding Token", e); // 예외 발생 시 로깅
            return ResponseEntity.ok(errorService.setError(2000,"jwt 토큰 해석 오류"));
        } catch(Exception e){
            //e.printStackTrace(); // 운영시는 가릴것
            logger.error("Unexpected error during changediary()", e); // 예외 발생 시 로깅
            return ResponseEntity.ok(errorService.setError(4000,"일기 패치 중 예상치 못한 에러발생"));
        }
    }

    @GetMapping
    public ResponseEntity<Object> getDiary(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
            @RequestParam(name = "emotion", required = false) String emotion){

        try {
            String token;
            if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
                token = jwtToken.replace("Bearer ", "");
            } else {
                token=jwtToken;
            }

            //jwtHandler.tokenValidation(token); //예외 1 가능
            int userId=jwtService.getUserIdFromJwt(jwtToken); //jwtHandler.jwtDecoder(token); //예외 2 가능


            List<String> emotionList = Arrays.asList("happy", "love", "expect","thanks","anger", "fear", "sad", "regret");
            boolean containsString = emotionList.contains(emotion);

            if (emotion == null || !containsString) {
                return ResponseEntity.ok(errorService.setError(2000,"형식 밖의 감정으로 인한 오류"));
            }

            Diary recommendation = diaryService.getRecommendedDiary(userId,emotion);

            if (recommendation != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("code", 1000);
                response.put("message", "Diary Load success");

                Map<String, Object> diaryInfo = new HashMap<>();
                diaryInfo.put("diaryid", recommendation.getDiaryId());
                diaryInfo.put("dcontent", recommendation.getDContent());
                diaryInfo.put("dtime", recommendation.getDiaryTime());
                diaryInfo.put("dtag", recommendation.getDTag());
                diaryInfo.put("userid", recommendation.getUser().getUserId());
                diaryInfo.put("openable", recommendation.getOpenable());

                response.put("data", diaryInfo);

                return ResponseEntity.ok(response); //일기 로딩 성공
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("code", 1005);
                response.put("message", "더 이상 추천할 해당 감정의 일기가 남아있지 않음");

                return ResponseEntity.ok(response); // 남은 공개된 일기가 없음
            }

        }catch(IllegalStateException e){
            logger.error("jwtToken is not in proper form / Outdated", e); // 예외 발생 시 로깅
            return ResponseEntity.ok(errorService.setError(2000,"jwt 토큰 형식 오류"));
        }catch(JwtException e){
            logger.error("Error during Decoding Token", e); // 예외 발생 시 로깅
            return ResponseEntity.ok(errorService.setError(2000,"jwt 토큰 해석 오류"));
        }catch(Exception e){
            logger.error("Unexpected exception occured loading diary", e);
            return ResponseEntity.ok(errorService.setError(4000,"일기 추천 중 예상치 못한 에러 발생"));
        }

    }

}
