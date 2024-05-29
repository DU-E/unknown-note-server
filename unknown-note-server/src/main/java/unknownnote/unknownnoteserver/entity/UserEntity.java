package unknownnote.unknownnoteserver.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 수정 아이디어
// USerEntity를 객체로 받아서 여기서 받은 데이터를 User 클래스로 넘겨준 뒤,
// User 클래스에서 UserEntity를 통해 필요한 데이터만 주입하는 방식
// 이 값을 repository를 통해 DB에 저장하거나, DB에서 가져오는 역할을 함

// @Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
// @Table(name = "user")
public class UserEntity {
    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    // @Column(name = "user_id")
    private int userId;

    // @Column(name = "method")
    private String method;
    // @Column(name = "social_id")
    private String socialId;
}
