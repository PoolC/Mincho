package org.poolc.api.activity.domain;

import lombok.Getter;
import org.poolc.api.activity.vo.ActivityUpdateValues;
import org.poolc.api.member.domain.Member;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "Activity")
@Getter
public class Activity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "title", columnDefinition = "varchar(1024)", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "varchar(1024)", nullable = false)
    private String description;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "host", referencedColumnName = "UUID", nullable = false)
    private Member host;

    @Column(name = "start_date", columnDefinition = "date")
    private LocalDate startDate;

    @Column(name = "class_hour", columnDefinition = "varchar(1024)")
    private String classHour;

    @Column(name = "is_seminar", columnDefinition = "boolean default false")
    private Boolean isSeminar;

    @Column(name = "capacity", nullable = false)
    private Long capacity;

    @Column(name = "hour", nullable = false)
    private Long hour;

    @Column(name = "available", columnDefinition = "boolean default false")
    private Boolean available;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActivityTag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActivityMember> members = new ArrayList<>();

    public Activity() {
    }

    public Activity(String title, String description, Member host, LocalDate startDate, String classHour, Boolean isSeminar, Long capacity, Long hour, Boolean available) {
        this.title = title;
        this.description = description;
        this.host = host;
        this.startDate = startDate;
        this.classHour = classHour;
        this.isSeminar = isSeminar;
        this.capacity = capacity;
        this.hour = hour;
        this.available = available;
    }

    @Transactional
    public void apply(ActivityMember activityMember) {
        this.members.add(activityMember);
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
                Objects.equals(getClassHour(), activity.getClassHour()) &&
                Objects.equals(getIsSeminar(), activity.getIsSeminar()) &&
                Objects.equals(getCapacity(), activity.getCapacity()) &&
                Objects.equals(getAvailable(), activity.getAvailable());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getHost(), getStartDate(), getClassHour(), getIsSeminar(), getCapacity(), getAvailable());
    }

    public void update(ActivityUpdateValues values) {
        this.title = values.getTitle();
        this.description = values.getDescription();
        this.startDate = values.getStartDate();
        this.classHour = values.getClassHour();
        this.isSeminar = values.getIsSeminar();
        this.capacity = values.getCapacity();
        this.hour = values.getHour();
        this.tags.clear();
        this.tags.addAll(values.getTags().stream()
                .map(t -> new ActivityTag(this, t)).collect(Collectors.toList()));
    }

}