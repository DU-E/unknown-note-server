package unknownnote.unknownnoteserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UserInfo {
    private int user_id;
    private String nickname;
    private String introduction;
}
