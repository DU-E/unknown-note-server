package unknownnote.unknownnoteserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import unknownnote.unknownnoteserver.entity.Diary;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Integer> {
    @Query("SELECT COUNT(d) FROM Diary d WHERE d.user.userId = :userId")
    int countByUserId(int userId);

    @Query("SELECT d.dTag FROM Diary d WHERE d.user.userId = :userId AND d.diaryTime BETWEEN :startDate AND :endDate")
    List<String> findTagsByUserIdAndMonth(@Param("userId") int userId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // 특정 사용자의 일기를 시간 기준으로 조회하는 예시 메서드
    List<Diary> findByUser_UserIdAndDiaryTimeBetween(int userId, Timestamp startTime, Timestamp endTime);

    @Query("SELECT d FROM Diary d " +
            "WHERE d.dTag = :emotion " +
            "AND d.openable = 1 "+
            "AND (d.diaryId NOT IN :viewedDiaryIds) " +
            "ORDER BY d.diaryTime DESC LIMIT 1")
    Diary findRecommendedDiary(@Param("emotion") String emotion,
                                     @Param("viewedDiaryIds") List<Integer> viewedDiaryIds); // viewedDiaryIds 가 빈 리스트가 아님

    @Query("SELECT d FROM Diary d WHERE d.dTag = :emotion AND d.openable = 1 ORDER BY d.diaryTime DESC LIMIT 1")
    Diary findDiariesEmptySituation(@Param("emotion") String emotion);  //viewedDiaryIds가 빈 리스트임
}

