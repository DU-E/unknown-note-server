package unknownnote.unknownnoteserver.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "diary")
@Getter
@Setter
public class Diary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private int diaryId;

    @Column(name = "d_content")
    private String dContent;

    @Column(name = "d_time")
    private Timestamp diaryTime;

    @Column(name = "d_tag")
    private String dTag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    @Column(name = "openable", nullable = false)
    private int openable;

}
