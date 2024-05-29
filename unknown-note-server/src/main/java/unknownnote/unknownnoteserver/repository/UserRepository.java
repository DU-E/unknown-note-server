package unknownnote.unknownnoteserver.repository;

import unknownnote.unknownnoteserver.entity.Essay;
import unknownnote.unknownnoteserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByMethodAndSocialId(String method, String socialId);
    User findByUserId(int userId);

}