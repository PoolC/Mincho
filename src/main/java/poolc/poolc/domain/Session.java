package poolc.poolc.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "Session")
@Getter
public class Session {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "activityID", referencedColumnName = "ID", nullable = false)
    private Activity activity;

    @Column(name = "description", columnDefinition = "varchar(1024)", nullable = false)
    private String description;

    @Column(name = "date", columnDefinition = "date",nullable = false)
    private LocalDate date;

    @Column(name = "sessionNumber", nullable = false)
    private Long sessionNumber;
}