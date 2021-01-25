package poolc.poolc.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "Attendance")
@Getter
public class Attendance {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "activityID", referencedColumnName = "ID", nullable = false)
    private Activity activity;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "sessionID", referencedColumnName = "ID", nullable = false)
    private Session session;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "memberID", referencedColumnName = "memberID", nullable = false)
    private ActivityMember memberID;


}