package unknownnote.unknownnoteserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Setter
@Getter
public class CalenderResponse {
    private int code;
    private String message;
    private List<CalenderInfo> data;

}
