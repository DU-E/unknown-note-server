package unknownnote.unknownnoteserver.controller;

import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import unknownnote.unknownnoteserver.dto.EssayDTO;
import unknownnote.unknownnoteserver.entity.Essay;
import unknownnote.unknownnoteserver.service.ErrorService;
import unknownnote.unknownnoteserver.service.EssayService;
import unknownnote.unknownnoteserver.service.JwtService;
import unknownnote.unknownnoteserver.entity.User;
import unknownnote.unknownnoteserver.dto.ErrorResponse;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/essay")
public class EssayController {

    private final EssayService essayService;
    private final JwtService jwtService;

    @Autowired
    private ErrorService errorService;

    @Autowired
    private JwtHandler jwtHandler;
    private static final Logger logger = LoggerFactory.getLogger(EssayController.class);

    @Autowired
    public EssayController(EssayService essayService, JwtService jwtService) {
        this.essayService = essayService;
        this.jwtService = jwtService;
    }

    @GetMapping
    public ResponseEntity<Object> getEssays(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken,
                                            @RequestParam(required = false) String category,
                                            @RequestParam(required = false) Integer page) {
        String token;
        if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
            token = jwtToken.replace("Bearer ", "");
        } else {
            token = jwtToken;
        }

        int userId;
        try {
            userId = jwtService.getUserIdFromJwt(token);
        } catch (Exception e) {
            return ResponseEntity.ok(errorService.setError(1005, "에세이 불러오기 실패"));
        }

        logger.debug("Querying essays with category: {}", category);
        if (category == null) {
            List<Essay> essays = essayService.findAll();
            if (!essays.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("code", 1000);
                response.put("message", "Fetched all essays");

                List<Map<String, Object>> essaysInfo = new ArrayList<>();
                for (Essay essay : essays) {
                    Map<String, Object> essayInfo = new HashMap<>();
                    essayInfo.put("essayid", essay.getEssayId());
                    essayInfo.put("eTitle", essay.getETitle());
                    essayInfo.put("eContent", essay.getEContent());
                    essayInfo.put("etime", essay.getEssayTime());
                    essayInfo.put("eCategory", essay.getECategory());
                    essayInfo.put("userid", essay.getUser().getUserId());
                    essayInfo.put("openable", essay.getOpenable());
                    essaysInfo.add(essayInfo);
                }

                response.put("data", essaysInfo);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.ok(errorService.setError(1005, "에세이 불러오기 실패"));
            }
        } else if (category.equals("favs")) {
            List<Essay> likedEssays = essayService.findAllLikedEssays(userId);
            if (!likedEssays.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("code", 1000);
                response.put("message", "Fetched liked essays");

                List<Map<String, Object>> essaysInfo = new ArrayList<>();
                for (Essay essay : likedEssays) {
                    Map<String, Object> essayInfo = new HashMap<>();
                    essayInfo.put("essayid", essay.getEssayId());
                    essayInfo.put("eTitle", essay.getETitle());
                    essayInfo.put("eContent", essay.getEContent());
                    essayInfo.put("etime", essay.getEssayTime());
                    essayInfo.put("eCategory", essay.getECategory());
                    essayInfo.put("userid", essay.getUser().getUserId());
                    essayInfo.put("openable", essay.getOpenable());
                    essaysInfo.add(essayInfo);
                }

                response.put("data", essaysInfo);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.ok(errorService.setError(1005, "에세이 불러오기 실패"));
            }
        } else if (category.equals("subs")) {
            List<Essay> essays = essayService.findAllEssaysBySubscribedUsers(userId);
            if (!essays.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("code", 1000);
                response.put("message", "Successfully fetched essays by subscribed users");

                List<Map<String, Object>> essaysInfo = new ArrayList<>();
                for (Essay essay : essays) {
                    Map<String, Object> essayInfo = new HashMap<>();
                    essayInfo.put("essayid", essay.getEssayId());
                    essayInfo.put("eTitle", essay.getETitle());
                    essayInfo.put("eContent", essay.getEContent());
                    essayInfo.put("etime", essay.getEssayTime());
                    essayInfo.put("eCategory", essay.getECategory());
                    essayInfo.put("userid", essay.getUser().getUserId());
                    essayInfo.put("openable", essay.getOpenable());
                    essaysInfo.add(essayInfo);
                }

                response.put("data", essaysInfo);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.ok(errorService.setError(1005, "에세이 불러오기 실패"));
            }
        } else if (category.equals("novel") || category.equals("poem") || category.equals("whisper")) {
            Page<Essay> essaysPage = essayService.findEssaysByCategory(category, page);
            if (essaysPage != null && !essaysPage.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("code", 1000);
                response.put("message", "Successfully fetched essays by category");

                List<Map<String, Object>> essaysInfo = new ArrayList<>();
                for (Essay essay : essaysPage.getContent()) {
                    Map<String, Object> essayInfo = new HashMap<>();
                    essayInfo.put("essayid", essay.getEssayId());
                    essayInfo.put("eTitle", essay.getETitle());
                    essayInfo.put("eContent", essay.getEContent());
                    essayInfo.put("etime", essay.getEssayTime());
                    essayInfo.put("eCategory", essay.getECategory());
                    essayInfo.put("userid", essay.getUser().getUserId());
                    essayInfo.put("openable", essay.getOpenable());
                    essaysInfo.add(essayInfo);
                }

                response.put("data", essaysInfo);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.ok(errorService.setError(1005, "에세이 불러오기 실패"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"code\": 1003, \"message\": \"Invalid type parameter\"}");
        }
    }

    @PostMapping
    public ResponseEntity<Object> saveEssay(@RequestBody EssayDTO essayDTO, @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken) {
        try {
            String token;
            if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
                token = jwtToken.replace("Bearer ", "");
            } else {
                token = jwtToken;
            }

            int userId = jwtService.getUserIdFromJwt(jwtToken);

            Essay savedEssay = essayService.saveNewEssay(essayDTO, userId);
            if (savedEssay != null) {
                return ResponseEntity.ok().body("{\"code\": 1000, \"message\": \"Essay saved\"}");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("{\"code\": 1003, \"message\": \"Essay saving failed\"}");
            }
        } catch (IllegalStateException e) {
            logger.error("jwtToken is not in proper form / Outdated", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"code\": 2000, \"message\": \"JwtToken is not in proper form / Outdated\"}");
        } catch (JwtException e) {
            logger.error("Error during Decoding Token", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"code\": 2000, \"message\": \"Error during Decoding Token\"}");
        } catch (RuntimeException e) {
            logger.error("Unexpected Error during saveNewEssay()", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"code\": 1003, \"message\": \" Unexpected Error during saveNewEssay()\"}");
        } catch (Exception e) {
            logger.error("Unexpected error during saving essay", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"code\": 4000, \"message\": \"Unexpected error during saving essay\"}");
        }
    }

    @PatchMapping
    public ResponseEntity<Object> updateEssay(@RequestBody Map<String, Object> requestBody, @RequestHeader("Authorization") String jwtToken) {
        try {
            Integer  essayId = (Integer) requestBody.get("essayid");
            if (essayId == null) {
                return ResponseEntity.badRequest().body("{\"code\": 400, \"message\": \"essayId is required\"}");
            }
            int openable = (int) requestBody.get("openable");
            String eContent = (String) requestBody.get("eContent");
            String eCategory = (String) requestBody.get("eCategory");
            String eTitle = (String) requestBody.get("eTitle");

            String token;
            if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
                token = jwtToken.replace("Bearer ", "");
            } else {
                token = jwtToken;
            }

            int userId = jwtService.getUserIdFromJwt(jwtToken);

            Essay updatedEssay = essayService.updateEssay(essayId, eContent, eCategory, eTitle, openable , userId);
            if (updatedEssay != null) {
                return ResponseEntity.ok().body("{\"code\": 1000, \"message\": \"Essay updated successfully\"}");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("{\"code\": 1003, \"message\": \"Essay update failed\"}");
            }
        } catch (IllegalStateException e) {
            logger.error("jwtToken is not in proper form / Outdated", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"code\": 2000, \"message\": \"JwtToken is not in proper form / Outdated\"}");
        } catch (JwtException e) {
            logger.error("Error during Decoding Token", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"code\": 2000, \"message\": \"Error during Decoding Token\"}");
        } catch (Exception e) {
            logger.error("Unexpected error during updateEssay()", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"code\": 4000, \"message\": \"Unexpected error during updating essay\"}");
        }
    }

    @PostMapping("/{essayId}/like")
    public ResponseEntity<Object> addLike(@PathVariable int essayId, @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken) {
        int userId = jwtService.getUserIdFromJwt(jwtToken);
        Essay essay = essayService.addLike(essayId, userId);
        if (essay != null) {
            return ResponseEntity.ok().body("{\"code\": 1000, \"message\": \"Successfully added like\"}");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"code\": 1003, \"message\": \"Failed to add like\"}");
        }
    }

    @DeleteMapping("/{essayId}/like")
    public ResponseEntity<Object> removeLike(@PathVariable int essayId, @RequestHeader(HttpHeaders.AUTHORIZATION) String jwtToken) {
        int userId = jwtService.getUserIdFromJwt(jwtToken);
        boolean success = essayService.removeLike(essayId, userId);
        if (success) {
            return ResponseEntity.ok().body("{\"code\": 1000, \"message\": \"Successfully removed like\"}");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"code\": 1003, \"message\": \"Failed to remove like\"}");
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserEssays(@PathVariable int userId, @RequestParam Optional<Integer> page) {
        List<Essay> essays = essayService.findUserEssays(page, userId);
        if (!essays.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 1000);

            List<Map<String, Object>> essaysInfo = new ArrayList<>();
            for (Essay essay : essays) {
                Map<String, Object> essayInfo = new HashMap<>();
                essayInfo.put("essayid", essay.getEssayId());
                essayInfo.put("etitle", essay.getETitle());
                essayInfo.put("econtent", essay.getEContent());
                essayInfo.put("etime", essay.getEssayTime());
                essayInfo.put("ecategory", essay.getECategory());

                User user = essay.getUser();
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("user_id", user.getUserId());
                userInfo.put("nickname", user.getNickname());
                userInfo.put("introduction", user.getIntroduction());

                essayInfo.put("user", userInfo);
                essaysInfo.add(essayInfo);
            }

            response.put("data", essaysInfo);

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.ok(errorService.setError(1005, "에세이 불러오기 실패"));
        }
    }

}
