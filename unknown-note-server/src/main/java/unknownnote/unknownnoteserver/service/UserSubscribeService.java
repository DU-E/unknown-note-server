package unknownnote.unknownnoteserver.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unknownnote.unknownnoteserver.entity.UserSubscribe;
import unknownnote.unknownnoteserver.repository.UserSubscribeRepository;

@Service
public class UserSubscribeService {

    @Autowired
    private UserSubscribeRepository userSubscribeRepository;

    public void subscribe(int targetUserId, int userId) {

        UserSubscribe userSubscribe = new UserSubscribe();
        userSubscribe.setUserId(targetUserId);
        userSubscribe.setFollowingId(userId);

        userSubscribeRepository.save(userSubscribe);
    }

    @Transactional
    public void unsubscribe(int userId, int followingId) {

        userSubscribeRepository.deleteByUserIdAndFollowingId(userId, followingId);
    }
}
