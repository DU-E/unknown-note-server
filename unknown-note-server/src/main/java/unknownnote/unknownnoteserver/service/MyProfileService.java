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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import java.nio.file.*;
import java.io.IOException;

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
            flower = "happy";
        else if(flowerTag.equals("love"))
            flower = "love";
        else if(flowerTag.equals("expect"))
            flower = "expect";
        else if(flowerTag.equals("thanks"))
            flower = "thanks";
        else if(flowerTag.equals("sad"))
            flower = "sad";
        else if(flowerTag.equals("anger"))
            flower = "anger";
        else if(flowerTag.equals("fear"))
            flower = "fear";
        else if(flowerTag.equals("regret"))
            flower = "regret";
        else
            flower = "happy";

        List<MonthlyActivity> activities = getMonthlyActivities(userId);
        List<RecentGraph> recentGraphs = getRecentGraphData(userId);

        String directoryPath = "./uploads/";
        String jpgFilePath = directoryPath + userId + ".jpg";
        String pngFilePath = directoryPath + userId + ".png";
        String img_name;

        // 파일 존재 여부 확인
        if (Files.exists(Paths.get(jpgFilePath))) {
            img_name = userId + ".jpg";
        } else if (Files.exists(Paths.get(pngFilePath))) {
            img_name = userId + ".png";
        } else {
            img_name = null;
        }
        String img_file = "http://13.48.223.79:8080/profile/files/" + img_name;

        UserInfo userInfo = new UserInfo(user.getUserId(), user.getNickname(), user.getIntroduction(), img_file);

        //MyprofileInfo 작성
        MyProfileInfo myProfileInfo = new MyProfileInfo();
        myProfileInfo.setUser(userInfo);
        myProfileInfo.setEssay_cnt(essayCount);
        myProfileInfo.setJournal_cnt(diaryCount);
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
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(30);

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atStartOfDay();

        // Diary 활동 조회
        List<Diary> diaries = diaryRepository.findByUser_UserIdAndDiaryTimeBetween(
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
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(90);

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atStartOfDay();

        List<Diary> diaries = diaryRepository.findByUser_UserIdAndDiaryTimeBetween(
                userId, Timestamp.valueOf(startDateTime), Timestamp.valueOf(endDateTime));

        List<Essay> essays = essayRepository.findByUser_UserIdAndEssayTimeBetween(
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
