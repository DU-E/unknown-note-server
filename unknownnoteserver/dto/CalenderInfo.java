package unknownnote.unknownnoteserver.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CalenderInfo {
    private int diaryid;
    private String dcontent;
    private LocalDateTime dtime;
    private String dtag;
    private int userid;
    private int openable;

}
