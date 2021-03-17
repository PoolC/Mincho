package org.poolc.api.activity.domain;

import lombok.Getter;
import org.poolc.api.activity.vo.SessionUpdateValues;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "Session")
@SequenceGenerator(
        name = "SESSION_SEQ_GENERATOR",
        sequenceName = "SESSION_SEQ"
)
@Getter
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SESSION_SEQ_GENERATOR")
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "activity_id", referencedColumnName = "id", nullable = false)
    private Activity activity;

    @Column(name = "description", columnDefinition = "varchar(1024)", nullable = false)
    private String description;

    @Column(name = "date", columnDefinition = "date", nullable = false)
    private LocalDate date;

    @Column(name = "session_number", nullable = false)
    private Long sessionNumber;

    @ElementCollection(fetch = LAZY)
    @CollectionTable(name = "attendance", joinColumns = @JoinColumn(name = "session_id"), uniqueConstraints = {@UniqueConstraint(columnNames = {"session_id", "member_loginid"})})
    @Column(name = "member_loginid")
    private List<String> attendedMemberLoginIDs = new ArrayList<>();

    public Session() {
    }

    public Session(Activity activity, String description, LocalDate date, Long sessionNumber) {
        this.activity = activity;
        this.description = description;
        this.date = date;
        this.sessionNumber = sessionNumber;
    }

    public void update(SessionUpdateValues values) {
        this.date = values.getDate();
        this.description = values.getDescription();
    }

    @Transactional
    public void clear() {
        this.getAttendedMemberLoginIDs().clear();
    }

    @Transactional
    public void attend(List<String> attendances) {
        this.attendedMemberLoginIDs.addAll(attendances);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Session session = (Session) o;
        return Objects.equals(getId(), session.getId()) &&
                Objects.equals(getActivity(), session.getActivity()) &&
                Objects.equals(getDescription(), session.getDescription()) &&
                Objects.equals(getDate(), session.getDate()) &&
                Objects.equals(getSessionNumber(), session.getSessionNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getActivity(), getDescription(), getDate(), getSessionNumber());
    }

}