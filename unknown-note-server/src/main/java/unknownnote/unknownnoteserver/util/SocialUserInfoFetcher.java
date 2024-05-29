package unknownnote.unknownnoteserver.util;

import org.springframework.stereotype.Component;
import unknownnote.unknownnoteserver.entity.UserEntity;
import unknownnote.unknownnoteserver.util.providers.GoogleUserInfoProvider;
import unknownnote.unknownnoteserver.util.providers.KakaoUserInfoProvider;
import unknownnote.unknownnoteserver.util.providers.NaverUserInfoProvider;

@Component
public class SocialUserInfoFetcher {
    public UserEntity fetchUserInfo(String accessToken, String provider) {
        UserInfoProvider userInfoProvider = switch (provider.toLowerCase()) {
            case "naver" -> new NaverUserInfoProvider();
            case "kakao" -> new KakaoUserInfoProvider();
            case "google" -> new GoogleUserInfoProvider();
            default -> throw new IllegalArgumentException("Unsupported provider: " + provider);
        };

        return userInfoProvider.fetchUserInfo(accessToken);
    }
}
