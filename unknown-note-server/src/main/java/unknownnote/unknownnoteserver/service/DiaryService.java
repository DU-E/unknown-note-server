package unknownnote.unknownnoteserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unknownnote.unknownnoteserver.dto.*;
import unknownnote.unknownnoteserver.entity.Diary;
import unknownnote.unknownnoteserver.entity.Monthly_emo;
import unknownnote.unknownnoteserver.repository.DiaryRepository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DiaryService {

    @Autowired
    private DiaryRepository diaryRepository;

    public List<String> getPreviousMonthTags(int userId) {
        // 현재 날짜를 기준으로 전 달의 첫 날과 마지막 날을 계산
        LocalDate now = LocalDate.now();
        LocalDate firstDayOfPreviousMonth = now.minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
        LocalDate lastDayOfPreviousMonth = now.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());

        // LocalDateTime으로 변환 (시간 범위를 포함시키기 위함)
        LocalDateTime startDateTime = firstDayOfPreviousMonth.atStartOfDay();
        LocalDateTime endDateTime = lastDayOfPreviousMonth.atTime(23, 59, 59);

        // 지정된 날짜 범위와 사용자 ID를 사용하여 d_tag 조회
        return diaryRepository.findTagsByUserIdAndMonth(userId, startDateTime, endDateTime);
    }

    public Monthly_emo getPreviousMonthEmotions(int userId) {
        List<String> tags = getPreviousMonthTags(userId); // 이전 달 태그 가져오기
        Monthly_emo monthlyEmo = new Monthly_emo();

        // 태그 리스트를 순회하며 감정 통계 업데이트
        for (String tag : tags) {
            monthlyEmo.incrementEmotion(tag);
        }

        return monthlyEmo;
    }

    public String findMostFrequentTag(int userId) {
        List<String> tags = getPreviousMonthTags(userId);

        if (tags.isEmpty()) {
            return "happy"; // 태그 리스트가 비어있으면 빈 문자열 반환
        }

        Map<String, Integer> tagCount = new HashMap<>();
        // 태그별로 출현 횟수를 계산
        for (String tag : tags) {
            tagCount.put(tag, tagCount.getOrDefault(tag, 0) + 1);
        }

        // 가장 많이 나온 태그 찾기
        Optional<Map.Entry<String, Integer>> mostFrequentTag = tagCount.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue());

        // 결과 반환
        return mostFrequentTag.map(Map.Entry::getKey).orElse("happy");
    }

    public List<CalenderInfo> getDiariesForMonth(int userId, int year, int month) {
        LocalDate startDate = YearMonth.of(year, month).atDay(1);
        LocalDate endDate = YearMonth.of(year, month).atEndOfMonth();

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        List<Diary> diaries = diaryRepository.findByUserIdAndDiaryTimeBetween(
                userId, Timestamp.valueOf(startDateTime), Timestamp.valueOf(endDateTime));

        return diaries.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private CalenderInfo convertToDto(Diary diary) {
        CalenderInfo dto = new CalenderInfo();
        dto.setDiaryid(diary.getDiaryId());
        dto.setDcontent(diary.getDContent());
        //한국 시간으로
        dto.setDtime(diary.getDiaryTime().toLocalDateTime().minusHours(9));
        dto.setDtag(diary.getDTag());
        dto.setUserid(diary.getUserId());
        dto.setOpenable(diary.getOpenable());
        return dto;
    }
}
