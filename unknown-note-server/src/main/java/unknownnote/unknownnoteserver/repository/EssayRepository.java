package unknownnote.unknownnoteserver.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import unknownnote.unknownnoteserver.entity.Essay;
import unknownnote.unknownnoteserver.entity.User;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public interface EssayRepository extends JpaRepository<Essay, Integer> {
    @Query("SELECT COUNT(e) FROM Essay e WHERE e.user.userId = :userId")
    int countByUserId(int userId);

    List<Essay> findByUser_UserIdAndEssayTimeBetween(int userId, Timestamp start, Timestamp end);
    Page<Essay> findByECategoryOrderByEssayTimeDesc(String category, Pageable pageable);
    List<Essay> findByUser(User user);

    Page<Essay> findByUser_UserId(int userId, Pageable pageable);
}