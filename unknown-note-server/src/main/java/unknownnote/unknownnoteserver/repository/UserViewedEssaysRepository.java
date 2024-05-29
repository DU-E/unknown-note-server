package unknownnote.unknownnoteserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import unknownnote.unknownnoteserver.entity.UserViewedEssaysEntity;
import unknownnote.unknownnoteserver.entity.UserViewedEssaysId;

import java.util.List;

public interface UserViewedEssaysRepository extends JpaRepository<UserViewedEssaysEntity, UserViewedEssaysId> {

    @Query("SELECT u.id.essayid FROM UserViewedEssaysEntity u WHERE u.id.userid = :userId")
    List<Integer> findViewedEssayIds(@Param("userId") int userId);
}