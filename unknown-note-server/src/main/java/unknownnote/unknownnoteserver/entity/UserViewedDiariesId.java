package unknownnote.unknownnoteserver.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserViewedDiariesId implements Serializable {

    @Column(name = "user_id")
    private int userid;

    @Column(name = "diary_id")
    private int diaryid;
}
