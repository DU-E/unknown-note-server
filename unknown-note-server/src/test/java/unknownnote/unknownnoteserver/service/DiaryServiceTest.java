package unknownnote.unknownnoteserver.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Collections;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import unknownnote.unknownnoteserver.repository.DiaryRepository;
import unknownnote.unknownnoteserver.repository.UserRepository;
import unknownnote.unknownnoteserver.entity.DiaryEntity;
import unknownnote.unknownnoteserver.entity.UserEntity;
import unknownnote.unknownnoteserver.dto.DiaryDTO;
import unknownnote.unknownnoteserver.repository.UserViewedDiariesRepository;
import unknownnote.unknownnoteserver.entity.UserViewedDiariesEntity;
import unknownnote.unknownnoteserver.entity.UserViewedDiariesId;


import java.util.Optional;
import java.sql.Timestamp;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DiaryServiceTest {

    @Mock
    private DiaryRepository diaryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserViewedDiariesRepository userViewedDiariesRepository;

    @InjectMocks
    private DiaryService diaryService;


    private String jwtSecret="kwangwoonboys2019DepartmentofSoftwareGraduationProject"; // mock 문제로 직접 선언 해줘야함
    //테스트에서만

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSaveNewDiary() {
        // Given
        DiaryDTO diaryDTO = new DiaryDTO();
        diaryDTO.setDcontent("테스트");
        diaryDTO.setDtag("행복");
        diaryDTO.setOpenable(1);

        String jwtToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyaWQiOjEyMywiaWF0IjoxNTE2MjM5MDIyfQ.rpxz19ZVQjAkw2E7D6F5DtAr7YrMrcmUwaTbhl4N3_E";


        int userId=123; // 예시


        // Mock UserRepository의 findById 메서드 호출 시 반환할 사용자 엔티티 생성
        UserEntity userEntity = new UserEntity();
        userEntity.setUserid(userId);
        userEntity.setNickname("Test User");
        userEntity.setUserpwd("Test Password");
        userEntity.setBirth(20000423);
        userEntity.setGender(3);
        userEntity.setMadedate(new Timestamp(System.currentTimeMillis()));
        userEntity.setLevel(12);
        userEntity.setIntroduction("test Intro");
        userEntity.setSocialid("test@social");
        userEntity.setMethod("sds");


        // Mock UserRepository의 findById 메서드가 사용자 엔티티를 반환하도록 설정
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        // Mock DiaryRepository의 save 메서드 호출 시 반환할 다이어리 엔티티 생성
        DiaryEntity savedDiaryEntity = new DiaryEntity();
        savedDiaryEntity.setDiaryid(1);
        savedDiaryEntity.setDcontent("테스트");
        savedDiaryEntity.setDtag("행복");
        savedDiaryEntity.setOpenable(1);
        savedDiaryEntity.setUser(userEntity);

        // Mock DiaryRepository의 save 메서드가 다이어리 엔티티를 반환하도록 설정
        when(diaryRepository.save(any(DiaryEntity.class))).thenReturn(savedDiaryEntity);

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        // When
        DiaryEntity result = diaryService.SaveNewDiary(diaryDTO, userId);

        assertEquals(savedDiaryEntity, result);

        // Then
        // 사용자 엔티티가 정상적으로 설정되었는지 확인
        assertEquals(userEntity, result.getUser());

        // DiaryRepository의 save 메서드가 1번 호출되었는지 확인
        verify(diaryRepository, times(1)).save(any(DiaryEntity.class));
    }

    @Test
    public void testGetRecommendedDiary() {
        // 주어진 상황(Given)
        String jwtToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyaWQiOjEyMywiaWF0IjoxNTE2MjM5MDIyfQ.rpxz19ZVQjAkw2E7D6F5DtAr7YrMrcmUwaTbhl4N3_E";
        String emotion = "happy";

        int userId = 123;

        // UserRepository의 findById 메서드가 사용자 엔티티를 반환하도록 설정
        UserEntity userEntity = new UserEntity();
        userEntity.setUserid(userId);
        userEntity.setNickname("Test User");
        userEntity.setUserpwd("Test Password");
        userEntity.setBirth(20000423);
        userEntity.setGender(3);
        userEntity.setMadedate(new Timestamp(System.currentTimeMillis()));
        userEntity.setLevel(12);
        userEntity.setIntroduction("test Intro");
        userEntity.setSocialid("test@social");
        userEntity.setMethod("sds");
        // 다른 사용자 속성 설정...

        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        // UserViewedDiariesRepository의 findViewedDiaryIds 메서드가 본 일기 ID 목록을 반환하도록 설정
        List<Integer> viewedDiaryIds = Collections.unmodifiableList(Arrays.asList(1, 2, 3));
        when(userViewedDiariesRepository.findViewedDiaryIds(userId)).thenReturn(viewedDiaryIds);

        // DiaryRepository의 findRecommendedDiary 메서드가 추천된 일기를 반환하도록 설정
        DiaryEntity recommendedDiary = new DiaryEntity();
        recommendedDiary.setDiaryid(4);
        recommendedDiary.setDcontent("추천된 테스트 일기");
        recommendedDiary.setDtag("happy");
        recommendedDiary.setDtime(new Timestamp(System.currentTimeMillis()));
        recommendedDiary.setOpenable(1);
        recommendedDiary.setUser(userEntity);

        //when(diaryRepository.findRecommendedDiary(emotion, viewedDiaryIds)).thenReturn(recommendedDiary);
        when(diaryRepository.findRecommendedDiary(eq("happy"), any(List.class)))
                .thenReturn(recommendedDiary);

        // When (실행)
        DiaryEntity result = diaryService.getRecommendedDiary(userId, emotion);

        // Then (검증)
        assertEquals(recommendedDiary, result);

        // 사용자 엔티티가 올바르게 설정되었는지 확인
        assertEquals(userEntity, result.getUser());

        // DiaryRepository의 findRecommendedDiary 메서드가 1번 호출되었는지 확인
        //verify(diaryRepository, times(1)).findRecommendedDiary(emotion, viewedDiaryIds);

        // UserViewedDiariesRepository의 save 메서드가 1번 호출되었는지 확인
        verify(userViewedDiariesRepository, times(1)).save(any(UserViewedDiariesEntity.class));
    }
}
