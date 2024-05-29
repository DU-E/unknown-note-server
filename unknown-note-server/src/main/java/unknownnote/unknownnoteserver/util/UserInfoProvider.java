package unknownnote.unknownnoteserver.util;

import unknownnote.unknownnoteserver.entity.UserEntity;

public interface UserInfoProvider {
    UserEntity fetchUserInfo(String accessToken);
}
