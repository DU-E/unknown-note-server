package unknownnote.unknownnoteserver.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyProfileResponse {
    private int code;
    private String message;
    private MyProfileInfo data;
}

