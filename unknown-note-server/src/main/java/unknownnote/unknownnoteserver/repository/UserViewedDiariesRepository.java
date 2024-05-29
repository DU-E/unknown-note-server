package unknownnote.unknownnoteserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import unknownnote.unknownnoteserver.entity.UserViewedDiariesEntity;
import unknownnote.unknownnoteserver.entity.UserViewedDiariesId;

import java.util.List;

public interface UserViewedDiariesRepository extends JpaRepository<UserViewedDiariesEntity, UserViewedDiariesId> {

    @Query("SELECT u.id.diaryid FROM UserViewedDiariesEntity u WHERE u.id.userid = :userId")
    List<Integer> findViewedDiaryIds(@Param("userId") int userId);
}