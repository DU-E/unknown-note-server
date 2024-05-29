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

    @Column(name = "e_time",columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp essayTime;

    @Column(name = "e_likes", columnDefinition = "INT DEFAULT 0")
    private int eLikes;

    @Column(name = "e_category")
    private String ECategory;

    @Column(name = "openable", columnDefinition = "INT DEFAULT 1")
    private int openable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

}
