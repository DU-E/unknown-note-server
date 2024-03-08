package unknownnote.unknownnoteserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import unknownnote.unknownnoteserver.dto.MonthlyActivity;
import unknownnote.unknownnoteserver.entity.Diary;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Integer> {
    @Query("SELECT COUNT(d) FROM Diary d WHERE d.userId = :userId")
    int countByUserId(int userId);

    @Query("SELECT d.dTag FROM Diary d WHERE d.userId = :userId AND d.diaryTime BETWEEN :startDate AND :endDate")
    List<String> findTagsByUserIdAndMonth(@Param("userId") int userId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // 특정 사용자의 일기를 시간 기준으로 조회하는 예시 메서드
    List<Diary> findByUserIdAndDiaryTimeBetween(int userId, Timestamp startTime, Timestamp endTime);

    List<Diary> findByUserId(int userId);
}

