package unknownnote.unknownnoteserver.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "diary")
@Data
public class DiaryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private Integer diaryid;

    @Column(name = "d_content", nullable = false)
    private String dcontent;

    @Column(name = "d_time", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp dtime;

    @Column(name = "d_tag", length = 50)
    private String dtag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    @Column(name = "openable", nullable = false)
    private int openable;

}
