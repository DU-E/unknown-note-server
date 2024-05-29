package unknownnote.unknownnoteserver.util.providers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import unknownnote.unknownnoteserver.entity.UserEntity;
import unknownnote.unknownnoteserver.util.UserInfoProvider;

public class KakaoUserInfoProvider implements UserInfoProvider {
    @Override
    public UserEntity fetchUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        String apiURL = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>("", headers); // GET 요청이므로 본문은 비움

        ResponseEntity<String> response = restTemplate.exchange(apiURL, HttpMethod.GET, entity, String.class);

        if(response.getStatusCode() == HttpStatus.OK) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(response.getBody());

                UserEntity user = new UserEntity();
                user.setSocialId(rootNode.get("id").asText());

                return user;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
