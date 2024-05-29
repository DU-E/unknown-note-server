package unknownnote.unknownnoteserver.util.providers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import unknownnote.unknownnoteserver.entity.UserEntity;
import unknownnote.unknownnoteserver.util.UserInfoProvider;

public class GoogleUserInfoProvider implements UserInfoProvider {
    @Override
    public UserEntity fetchUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        String apiURL = "https://www.googleapis.com/oauth2/v3/userinfo";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>("", headers);

        ResponseEntity<String> response = restTemplate.exchange(apiURL, HttpMethod.GET, entity, String.class);

        if(response.getStatusCode() == HttpStatus.OK) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(response.getBody());

                UserEntity user = new UserEntity();
                user.setSocialId(rootNode.get("email").asText());

                return user;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
