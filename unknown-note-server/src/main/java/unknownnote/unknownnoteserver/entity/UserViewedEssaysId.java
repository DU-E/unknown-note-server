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
public class UserViewedEssaysId implements Serializable {

    @Column(name = "user_id")
    private int userid;

    @Column(name = "essay_id")
    private int essayid;
}