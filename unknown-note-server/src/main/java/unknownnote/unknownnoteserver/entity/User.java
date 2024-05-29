package unknownnote.unknownnoteserver.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
@Data // Lombok 대체  getters, setters, toString, equals, hashCode
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId; // I 대분자로 바꿈(변경사항)

    @Column(name = "nickname", columnDefinition = "VARCHAR(50) DEFAULT 'empty'")
    private String nickname;

    @Column(name = "birth", columnDefinition = "INT DEFAULT 0")
    private int birth;

    @Column(name = "gender", columnDefinition = "INT DEFAULT 0")
    private int gender;

    @Column(name = "made_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp madedate;

    @Column(name = "social_id", columnDefinition = "VARCHAR(250) DEFAULT 'empty'")
    private String socialId; // I 대분자로 바꿈(변경사항)

    @Column(name = "introduction", columnDefinition = "VARCHAR(150) DEFAULT 'empty'")
    private String introduction;

    @Column(name = "method", columnDefinition = "VARCHAR(100) DEFAULT 'empty'")
    private String method;

    @Column(name = "profile_img_url", columnDefinition = "VARCHAR(200) DEFAULT 'empty'")
    private String profileImgUrl;

    @ManyToMany
    @JoinTable(
            name = "user_liked_essays",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "essay_id")
    )
    private List<Essay> likedEssays = new ArrayList<>();

}
