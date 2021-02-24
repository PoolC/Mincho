package org.poolc.api.activity.domain;

import lombok.Getter;
import org.poolc.api.activity.vo.SessionUpdateValues;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

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

    @Column(name = "date", columnDefinition = "date", nullable = false)
    private LocalDate date;

    @Column(name = "sessionNumber", nullable = false)
    private Long sessionNumber;

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