package org.poolc.api.activity.domain;

import lombok.Builder;
import lombok.Getter;
import org.poolc.api.activity.vo.ActivityCreateValues;
import org.poolc.api.activity.vo.ActivityUpdateValues;
import org.poolc.api.member.domain.Member;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;

@Entity(name = "Activity")
@SequenceGenerator(
        name = "ACTIVITY_SEQ_GENERATOR",
        sequenceName = "ACTIVITY_SEQ"
)
@Getter
@Builder
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACTIVITY_SEQ_GENERATOR")
    @Column(name = "id")
    private Long id;

    @Column(name = "title", nullable = false, columnDefinition = "varchar(1024)")
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "text")
    private String description;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "host", nullable = false, referencedColumnName = "UUID")
    private Member host;

    @Column(name = "start_date", nullable = false, columnDefinition = "date")
    private LocalDate startDate;

    @Column(name = "class_hour", nullable = false, columnDefinition = "varchar(1024)")
    private String classHour;

    @Column(name = "is_seminar", nullable = false, columnDefinition = "boolean default false")
    private Boolean isSeminar;

    @Column(name = "capacity", nullable = false)
    private Long capacity;

    @Column(name = "hour", nullable = false)
    private Long hour;

    @Column(name = "available", nullable = false, columnDefinition = "boolean default false")
    private Boolean available;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActivityTag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Session> sessions = new ArrayList<>();

    @ElementCollection(fetch = EAGER)
    @CollectionTable(name = "activity_members", joinColumns = @JoinColumn(name = "activity_id"), uniqueConstraints = {@UniqueConstraint(columnNames = {"activity_id", "member_login_id"})})
    @Column(name = "member_login_id")
    private List<String> memberLoginIDs = new ArrayList<>();

    @ElementCollection(fetch = LAZY)
    @CollectionTable(name = "activity_file_list", joinColumns = @JoinColumn(name = "activity_id"), uniqueConstraints = {@UniqueConstraint(columnNames = {"activity_id", "file_uri"})})
    @Column(name = "file_uri", columnDefinition = "varchar(1024)")
    private List<String> fileList = new ArrayList<>();

    public Activity() {
    }

    public Activity(String title, String description, Member host, LocalDate startDate, String classHour, Boolean isSeminar, Long capacity, Long hour, Boolean available, List<String> fileList) {
        this.title = title;
        this.description = description;
        this.host = host;
        this.startDate = startDate;
        this.classHour = classHour;
        this.isSeminar = isSeminar;
        this.capacity = capacity;
        this.hour = hour;
        this.available = available;
        this.fileList = fileList;
    }

    public Activity(Member member, ActivityCreateValues values) {
        this.title = values.getTitle();
        this.description = values.getDescription();
        this.host = member;
        this.startDate = values.getStartDate();
        this.classHour = values.getClassHour();
        this.isSeminar = values.getIsSeminar();
        this.classHour = values.getClassHour();
        this.capacity = values.getCapacity();
        this.hour = values.getHour();
        this.available = false;
        if (values.getFileList() != null) {
            checkDuplicateFileList(values.getFileList());
            this.fileList = values.getFileList();
        }
    }

    public Activity(Long id, String title, String description, Member host, LocalDate startDate, String classHour, Boolean isSeminar, Long capacity, Long hour, Boolean available, List<ActivityTag> tags, List<Session> sessions, List<String> memberLoginIDs, List<String> fileList) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.host = host;
        this.startDate = startDate;
        this.classHour = classHour;
        this.isSeminar = isSeminar;
        this.capacity = capacity;
        this.hour = hour;
        this.available = available;
        this.tags = tags;
        this.sessions = sessions;
        this.memberLoginIDs = memberLoginIDs;
        this.fileList = fileList;
    }

    @Transactional
    public void toggleApply(String login_id) {
        if (this.memberLoginIDs.contains(login_id)) {
            this.memberLoginIDs.remove(login_id);
            return;
        }
        this.memberLoginIDs.add(login_id);
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
        if (values.getFileList() != null) {
            checkDuplicateFileList(values.getFileList());
            this.fileList = values.getFileList();
        }
    }

    public void open() {
        this.available = true;
    }

    public void close() {
        this.available = false;
    }

    public boolean checkMemberContain(String loginId) {
        return memberLoginIDs.contains(loginId);
    }

    private void checkDuplicateFileList(List<String> fileList) {
        boolean duplicated = fileList.stream().distinct().count() != fileList.size();
        if (duplicated) {
            throw new IllegalArgumentException("파일 URI가 중복되었습니다.");
        }
    }
}