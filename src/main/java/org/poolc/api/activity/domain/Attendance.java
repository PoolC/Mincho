package org.poolc.api.activity.domain;

import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "Attendance")
@Getter
public class Attendance {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "session_id", referencedColumnName = "id", nullable = false)
    private Session session;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "member_id", nullable = false)
    private ActivityMember memberID;

    public Attendance(Session session, ActivityMember memberID) {
        this.session = session;
        this.memberID = memberID;
    }

    protected Attendance() {

    }
}