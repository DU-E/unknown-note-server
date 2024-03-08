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
    @Column(name = "user_id")
    private int userId;
    @Column(name = "openable")
    private int openable;

}
