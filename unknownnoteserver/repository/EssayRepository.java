package unknownnote.unknownnoteserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import unknownnote.unknownnoteserver.dto.MonthlyActivity;
import unknownnote.unknownnoteserver.entity.Essay;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public interface EssayRepository extends JpaRepository<Essay, Integer> {
    @Query("SELECT COUNT(e) FROM Essay e WHERE e.userId = :userId")
    int countByUserId(int userId);

    // 특정 사용자의 에세이를 시간 기준으로 조회하는 예시 메서드
    List<Essay> findByUserIdAndEssayTimeBetween(int userId, Timestamp start, Timestamp end);

}