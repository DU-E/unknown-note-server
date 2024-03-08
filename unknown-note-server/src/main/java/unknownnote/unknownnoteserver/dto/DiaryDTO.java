package unknownnote.unknownnoteserver.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class DiaryDTO {

    private String dcontent;
    private String dtag;
    private int openable;


}