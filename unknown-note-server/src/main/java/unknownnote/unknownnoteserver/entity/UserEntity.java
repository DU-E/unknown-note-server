package unknownnote.unknownnoteserver.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserEntity {
    // 데이터 베이스에 테이블이 있으면 그에 해당하는 entity가 있어야 함.
    @Id // 엔티티는 필수적으로 Id가 있어야 함.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    private String method;
    private String socialId;
}
