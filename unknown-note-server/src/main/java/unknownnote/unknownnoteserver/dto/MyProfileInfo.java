package unknownnote.unknownnoteserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import unknownnote.unknownnoteserver.entity.Monthly_emo;
import unknownnote.unknownnoteserver.entity.User;

import java.util.List;

@Getter
@Setter
public class MyProfileInfo {
    private UserInfo user;
    private int essayCnt;
    private int journalCnt;

    private List<RecentGraph>  recent_graph;
    private List<MonthlyActivity> monthly_act;
    private Monthly_emo monthly_emo;
    private String flower;
    private int is_subscribed;

}
