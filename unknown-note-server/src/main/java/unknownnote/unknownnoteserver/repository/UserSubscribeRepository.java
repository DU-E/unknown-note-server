package unknownnote.unknownnoteserver.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import unknownnote.unknownnoteserver.entity.UserSubscribe;

public interface UserSubscribeRepository extends JpaRepository<UserSubscribe, Integer> {
    @Transactional
    void deleteByUserIdAndFollowingId(int userId, int followingId);
}