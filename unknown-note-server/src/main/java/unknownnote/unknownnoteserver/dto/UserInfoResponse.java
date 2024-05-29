package unknownnote.unknownnoteserver.dto;

import lombok.Getter;
import lombok.Setter;
import unknownnote.unknownnoteserver.entity.User;

@Setter
@Getter
public class UserInfoResponse {
    private int code;
    private String message;
    private UserInfo data;
}
