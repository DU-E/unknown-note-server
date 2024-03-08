package unknownnote.unknownnoteserver.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unknownnote.unknownnoteserver.dto.*;
import unknownnote.unknownnoteserver.entity.Diary;
import unknownnote.unknownnoteserver.entity.Essay;
import unknownnote.unknownnoteserver.entity.Monthly_emo;
import unknownnote.unknownnoteserver.entity.User;
import unknownnote.unknownnoteserver.repository.DiaryRepository;
import unknownnote.unknownnoteserver.repository.EssayRepository;
import unknownnote.unknownnoteserver.repository.UserRepository;
import unknownnote.unknownnoteserver.repository.UserSubscribeRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class MyProfileService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EssayRepository essayRepository;
    @Autowired
    private DiaryRepository diaryRepository;
    @Autowired
    private DiaryService diaryService;
    @Autowired
    private UserSubscribeRepository userSubscribeRepository;

    public MyProfileResponse getMyProfileInfo(int userId, int otherUserId, boolean meWatchingMyProfile) {
        //유저 할당
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        //일기, 수필 개수 할당
        int essayCount = essayRepository.countByUserId(userId);
        int diaryCount = diaryRepository.countByUserId(userId);
        //이달의 감정 통계 할당
        Monthly_emo monthly_emo = diaryService.getPreviousMonthEmotions(userId);
        //이달의 꽃 할당
        String flowerTag = diaryService.findMostFrequentTag(userId);
        String flower;
        if(flowerTag.equals("happy"))
            flower = "복수초";
        else if(flowerTag.equals("love"))
            flower = "장미";
        else if(flowerTag.equals("expect"))
            flower = "프리지아";
        else if(flowerTag.equals("thanks"))
            flower = "민들레";
        else if(flowerTag.equals("sad"))
            flower = "메리골드";
        else if(flowerTag.equals("anger"))
            flower = "클로버";
        else if(flowerTag.equals("fear"))
            flower = "덧나무";
        else if(flowerTag.equals("regret"))
            flower = "라일락";
        else
            flower = "복수초";

        List<MonthlyActivity> activities = getMonthlyActivities(userId);
        List<RecentGraph> recentGraphs = getRecentGraphData(userId);

        UserInfo userInfo = new UserInfo(user.getUserid(), user.getNickname(), user.getIntroduction());

        //MyprofileInfo 작성
        MyProfileInfo myProfileInfo = new MyProfileInfo();
        myProfileInfo.setUser(userInfo);
        myProfileInfo.setEssayCnt(essayCount);
        myProfileInfo.setJournalCnt(diaryCount);
        myProfileInfo.setMonthly_emo(monthly_emo);
        myProfileInfo.setFlower(flower);
        myProfileInfo.setMonthly_act(activities);
        myProfileInfo.setRecent_graph(recentGraphs);
        if(meWatchingMyProfile == true){ //내가 내 프로필 보는 상황
            myProfileInfo.setIs_subscribed(0);
        }
        else{ //내가 남 프로필 보는 상황
            boolean exits = userSubscribeRepository.findByUserIdAndFollowingId(otherUserId, userId).isPresent();
            if(exits)
                myProfileInfo.setIs_subscribed(1);
            else
                myProfileInfo.setIs_subscribed(0);
        }


        //MyProfileResponse 작성
        MyProfileResponse response = new MyProfileResponse();
        response.setCode(1000); // Success code
        response.setMessage("");
        response.setData(myProfileInfo);

        return response;
    }
    public List<RecentGraph> getRecentGraphData(int userId) {
        LocalDate endDate = LocalDate.now().plusDays(3);
        LocalDate startDate = endDate.minusMonths(3);

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atStartOfDay();

        // Diary 활동 조회
        List<Diary> diaries = diaryRepository.findByUser_UseridAndDiaryTimeBetween(
                userId, Timestamp.valueOf(startDateTime), Timestamp.valueOf(endDateTime));

        // 날짜별로 집계 및 value 계산
        Map<LocalDate, Double> aggregatedData = diaries.stream()
                .collect(Collectors.groupingBy(
                        diary -> diary.getDiaryTime().toInstant().atZone(ZoneId.of("UTC")).toLocalDate(),
                        Collectors.summingDouble(diary -> calculateValue(diary.getDTag()))
                ));

        // 결과 매핑
        List<RecentGraph> result = aggregatedData.entrySet().stream()
                .map(entry -> new RecentGraph(Timestamp.from(entry.getKey().atStartOfDay(ZoneId.of("UTC")).toInstant()), entry.getValue()))
                .sorted(Comparator.comparing(RecentGraph::getDate))
                .collect(Collectors.toList());

        //감정 수치 연산
        result.forEach(data -> {
            if (data.getValue() > 5.0) {
                data.setValue(1.0);
            } else if (data.getValue() >= 3.0) {
                data.setValue(0.7);
            } else if (data.getValue() >= 2.0) {
                data.setValue(0.5);
            } else if (data.getValue() >= 1.0) {
                data.setValue(0.3);
            } else if (data.getValue() < -5.0) {
                data.setValue(-1.0);
            } else if (data.getValue() <= -3.0) {
                data.setValue(-0.7);
            } else if (data.getValue() <= -2.0) {
                data.setValue(-0.5);
            } else if (data.getValue() <= -1.0) {
                data.setValue(-0.3);
            }
        });

        return result;

    }


    private double calEmotionRate(double value){

        return 1.0;
    }

    private double calculateValue(String eTag) {
        List<String> positiveTags = List.of("happy", "love", "anticipation", "thank");
        List<String> negativeTags = List.of("sad", "anger", "fear", "regret");

        if (positiveTags.contains(eTag)) {
            return 1.0;
        } else if (negativeTags.contains(eTag)) {
            return -1.0;
        }
        return 0.0; // Default value if eTag does not match any category
    }

    public List<MonthlyActivity> getMonthlyActivities(int userId) {
        LocalDate endDate = LocalDate.now().plusDays(3);
        LocalDate startDate = endDate.minusMonths(3);

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atStartOfDay();

        List<Diary> diaries = diaryRepository.findByUser_UseridAndDiaryTimeBetween(
                userId, Timestamp.valueOf(startDateTime), Timestamp.valueOf(endDateTime));

        List<Essay> essays = essayRepository.findByUserIdAndEssayTimeBetween(
                userId, Timestamp.valueOf(startDateTime), Timestamp.valueOf(endDateTime));

        Stream<Instant> combinedActivities = Stream.concat(
                diaries.stream().map(diary -> diary.getDiaryTime().toInstant()),
                essays.stream().map(essay -> essay.getEssayTime().toInstant())
        );

        Map<LocalDate, Long> activitiesCount = combinedActivities.collect(
                Collectors.groupingBy(instant -> instant.atZone(ZoneId.of("UTC")).toLocalDate(), Collectors.counting())
        );

        List<MonthlyActivity> activities = activitiesCount.entrySet().stream()
                .map(entry -> new MonthlyActivity(
                        Timestamp.from(entry.getKey().atStartOfDay(ZoneId.of("UTC")).toInstant()),
                        entry.getValue().intValue()))
                .sorted(Comparator.comparing(MonthlyActivity::getDate))
                .collect(Collectors.toList());

        return activities;
    }
}
