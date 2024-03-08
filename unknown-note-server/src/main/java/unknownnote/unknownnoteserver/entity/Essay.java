package unknownnote.unknownnoteserver.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "essay")
@Setter
@Getter
public class Essay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "essay_id")
    private int essayId;

    @Column(name = "e_title")
    private String eTitle;
    @Column(name = "e_content")
    private String eContent;
    @Column(name = "e_time")
    private Timestamp essayTime;
    @Column(name = "e_likes")
    private int eLikes;
    @Column(name = "e_category")

    private String eCategory;
    @Column(name = "user_id")
    private int userId;

}
