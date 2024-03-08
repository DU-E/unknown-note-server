package unknownnote.unknownnoteserver.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import unknownnote.unknownnoteserver.repository.DiaryRepository;

@Component
@Getter
@Setter
public class Diary_cnt {
    private int diary_cnt;

    @Autowired
    private DiaryRepository diaryRepository;

}
