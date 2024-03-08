package unknownnote.unknownnoteserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unknownnote.unknownnoteserver.dto.UserInfo;
import unknownnote.unknownnoteserver.dto.UserInfoRequest;
import unknownnote.unknownnoteserver.dto.UserInfoResponse;
import unknownnote.unknownnoteserver.entity.User;
import unknownnote.unknownnoteserver.exception.UserNotFoundException;
import unknownnote.unknownnoteserver.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserInfoResponse getUserInfo(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("해당 ID의 유저가 없습니다"));

        UserInfo userInfo = new UserInfo(user.getUserid(), user.getNickname(), user.getIntroduction());

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
