package poolc.poolc.domain;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity(name = "Activity")
@Getter
public class Activity {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "title", columnDefinition = "varchar(1024)", nullable = false)
    private String title;

    @Column(name = "host", columnDefinition = "varchar(40)", nullable = false)
    private String host;

    @Column(name = "startDate", columnDefinition = "date")
    private LocalDate startDate;

    @Column(name = "endDate", columnDefinition = "date")
    private LocalDate endDate;

    @Column(name = "classHour", columnDefinition = "varchar(1024)")
    private String classHour;

    @Column(name = "isSeminar",columnDefinition = "boolean default false")
    private Boolean isSeminar;

    @Column(name = "capacity", nullable = false)
    private Long capacity;

    @Column(name = "available",columnDefinition = "boolean default false")
    private Boolean available;
}