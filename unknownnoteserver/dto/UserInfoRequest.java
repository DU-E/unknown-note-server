package unknownnote.unknownnoteserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserInfoRequest {
    private int userId;
    private String nickname;
    private String introduction;
}
