package unknownnote.unknownnoteserver.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_subscribe")
@IdClass(UserSubscribeId.class) // 복합 키 클래스 지정
@Getter
@Setter
public class UserSubscribe {

    @Id // 복합 키의 일부인 user_id
    @Column(name = "user_id")
    private int userId;

    @Id // 복합 키의 일부인 following_id
    @Column(name = "following_id")
    private int followingId;


}