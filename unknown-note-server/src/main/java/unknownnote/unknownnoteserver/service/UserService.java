package unknownnote.unknownnoteserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unknownnote.unknownnoteserver.dto.UserInfo;
import unknownnote.unknownnoteserver.dto.UserInfoRequest;
import unknownnote.unknownnoteserver.dto.UserInfoResponse;
import unknownnote.unknownnoteserver.entity.User;
import unknownnote.unknownnoteserver.entity.UserEntity;
import unknownnote.unknownnoteserver.exception.UserNotFoundException;
import unknownnote.unknownnoteserver.jwt.JWTUtil;
import unknownnote.unknownnoteserver.repository.UserRepository;
import unknownnote.unknownnoteserver.util.SocialUserInfoFetcher;

import java.sql.Timestamp;
import java.time.Instant;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private SocialUserInfoFetcher fetcher;

    public UserEntity processLogin(String method, String accessToken) {
        UserEntity userInfo = fetcher.fetchUserInfo(accessToken, method);
        User existData = userRepository.findByMethodAndSocialId(method, userInfo.getSocialId());

        if (existData == null) {
            User user = new User();
            user.setMethod(method);
            user.setSocialId(userInfo.getSocialId());

            // primiteve type은 null이 될 수 없음 문제가 발생
            // 따라서 임의 값을 일단 설정해두겠음
            user.setBirth(1);
            user.setGender(1);
            user.setNickname("test");
            user.setProfileImgUrl("test");
            user.setIntroduction("test");
            user.setMadedate(Timestamp.from(Instant.now()));

            userRepository.save(user);

            // user.getUserId()가 null이라서 오류가 발생할 수 있음 -> 발생안함
            UserEntity userEntity = new UserEntity(user.getUserId(), method, userInfo.getSocialId());

            return userEntity;
        }

        UserEntity userEntity = new UserEntity(existData.getUserId(), method, userInfo.getSocialId());

        return userEntity;
    }

    public String generateJwtToken(int userId) {
        return jwtUtil.createJwt(userId, 60*60*60*60L);
    }

    // 태상 + 동규 코드 추가
    public UserInfoResponse getUserInfo(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("해당 ID의 유저가 없습니다"));

        UserInfo userInfo = new UserInfo(user.getUserId(), user.getNickname(), user.getIntroduction());

        UserInfoResponse response = new UserInfoResponse();
        response.setCode(1000); // Success code
        response.setMessage("");
        response.setData(userInfo);

        return response;
    }

    public void patchUserInfo(int user_id, UserInfoRequest userInfoRequest){
        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new UserNotFoundException("해당 ID의 유저가 없습니다"));

        // 사용자 정보 업데이트
        user.setNickname(userInfoRequest.getNickname());
        user.setIntroduction(userInfoRequest.getIntroduction());

        // 변경 사항 저장
        userRepository.save(user);
    }
}
