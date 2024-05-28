package unknownnote.unknownnoteserver.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.HttpHeaders;


import unknownnote.unknownnoteserver.dto.DiaryDTO;
import unknownnote.unknownnoteserver.entity.DiaryEntity;
import unknownnote.unknownnoteserver.service.DiaryService;
import unknownnote.unknownnoteserver.entity.UserEntity;


import java.sql.Timestamp;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class DiaryControllerTest {

    @Mock
    private DiaryService diaryService;

    @InjectMocks
    private DiaryController diaryController;

    private MockMvc mockMvc;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(diaryController).build();
    }

    @Test
    public void testSaveDiaryEntry() throws Exception {
        // Given
        DiaryDTO diaryDTO = new DiaryDTO();
        diaryDTO.setDcontent("테스트");
        diaryDTO.setDtag("행복");
        diaryDTO.setOpenable(1);

        String jwtToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyaWQiOjEyMywiaWF0IjoxNTE2MjM5MDIyfQ.rpxz19ZVQjAkw2E7D6F5DtAr7YrMrcmUwaTbhl4N3_E"; // Add a valid JWT token

        when(diaryService.SaveNewDiary(any(DiaryDTO.class), any(int.class))).thenReturn(new DiaryEntity());

        // When
        mockMvc.perform(post("/diary/save")
                        .header("Authorization", jwtToken)
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(diaryDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"code\": 1000, \"message\": \"diary saved\"}"))
                .andReturn();

        // Then
        verify(diaryService, times(1)).SaveNewDiary(any(DiaryDTO.class), any(int.class));

        // Additional checks if needed
    }

    @Test
    public void testGetRecommendedDiary() throws Exception {
        // Given
        String jwtToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyaWQiOjEyMywiaWF0IjoxNTE2MjM5MDIyfQ.rpxz19ZVQjAkw2E7D6F5DtAr7YrMrcmUwaTbhl4N3_E"; // Add a valid JWT token

        UserEntity userEntity = new UserEntity();
        userEntity.setUserid(123);
        userEntity.setNickname("Test User");
        userEntity.setUserpwd("Test Password");
        userEntity.setBirth(20000423);
        userEntity.setGender(3);
        userEntity.setMadedate(new Timestamp(System.currentTimeMillis()));
        userEntity.setLevel(12);
        userEntity.setIntroduction("test Intro");
        userEntity.setSocialid("test@social");
        userEntity.setMethod("sds");


        // Mock the service method to return a dummy DiaryEntity
        DiaryEntity dummyDiary = new DiaryEntity();
        dummyDiary.setDiaryid(1);
        dummyDiary.setDcontent("Test Diary Content");
        dummyDiary.setDtime(new Timestamp(System.currentTimeMillis()));
        dummyDiary.setDtag("happy");
        dummyDiary.setUser(userEntity);
        dummyDiary.setOpenable(1);
        // Set other fields as needed

        when(diaryService.getRecommendedDiary(any(int.class), any(String.class))).thenReturn(dummyDiary);

        // When and Then
        mockMvc.perform(get("/diary")
                        .header(HttpHeaders.AUTHORIZATION, jwtToken)
                        .param("emotion", "happy"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.message").value("diary load success"))
                .andExpect(jsonPath("$.diary.diaryid").value(dummyDiary.getDiaryid()))
                .andExpect(jsonPath("$.diary.dcontent").value(dummyDiary.getDcontent()))
                // Add other fields as needed
                .andReturn();

        // Verify that the service method is called
        verify(diaryService, times(1)).getRecommendedDiary(any(int.class), any(String.class));
    }

}
