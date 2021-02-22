package org.poolc.api.domain;

import lombok.Getter;
import org.poolc.api.member.domain.Member;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "Activity")
@Getter
public class Activity {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "title", columnDefinition = "varchar(1024)", nullable = false)
    private String title;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "host", referencedColumnName = "UUID", nullable = false)
    private Member host;

    @Column(name = "startDate", columnDefinition = "date")
    private LocalDate startDate;

    @Column(name = "endDate", columnDefinition = "date")
    private LocalDate endDate;

    @Column(name = "classHour", columnDefinition = "varchar(1024)")
    private String classHour;

    @Column(name = "isSeminar", columnDefinition = "boolean default false")
    private Boolean isSeminar;

    @Column(name = "capacity", nullable = false)
    private Long capacity;

    @Column(name = "available", columnDefinition = "boolean default false")
    private Boolean available;

    public Activity() {
    }

    public Activity(String title, Member host, LocalDate startDate, LocalDate endDate, String classHour, Boolean isSeminar, Long capacity, Boolean available) {
        this.title = title;
        this.host = host;
        this.startDate = startDate;
        this.endDate = endDate;
        this.classHour = classHour;
        this.isSeminar = isSeminar;
        this.capacity = capacity;
        this.available = available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activity activity = (Activity) o;
        return Objects.equals(getId(), activity.getId()) &&
                Objects.equals(getTitle(), activity.getTitle()) &&
                Objects.equals(getHost(), activity.getHost()) &&
                Objects.equals(getStartDate(), activity.getStartDate()) &&
                Objects.equals(getEndDate(), activity.getEndDate()) &&
                Objects.equals(getClassHour(), activity.getClassHour()) &&
                Objects.equals(getIsSeminar(), activity.getIsSeminar()) &&
                Objects.equals(getCapacity(), activity.getCapacity()) &&
                Objects.equals(getAvailable(), activity.getAvailable());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getHost(), getStartDate(), getEndDate(), getClassHour(), getIsSeminar(), getCapacity(), getAvailable());
    }
}