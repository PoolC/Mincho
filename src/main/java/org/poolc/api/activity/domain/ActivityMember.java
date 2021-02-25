package org.poolc.api.activity.domain;

import lombok.Getter;
import org.poolc.api.member.domain.Member;

import javax.persistence.*;
import java.io.Serializable;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "ActivityMember")
@Table(name = "ActivityMember",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"activity_id", "member_id"})})
@Getter
public class ActivityMember implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "activity_id", referencedColumnName = "id", nullable = false)
    private Activity activity;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "UUID", nullable = false)
    private Member member;

    public ActivityMember(Activity activity, Member member) {
        this.activity = activity;
        this.member = member;
    }

    protected ActivityMember() {

    }
}