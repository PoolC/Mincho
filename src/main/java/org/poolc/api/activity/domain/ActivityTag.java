package org.poolc.api.activity.domain;

import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "ActivityTag")
@Getter
@Table(name = "ActivityTag",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"activityID", "tagID"})})
public class ActivityTag {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "activityID", referencedColumnName = "ID", nullable = false)
    private Activity activity;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "tagID", referencedColumnName = "ID", nullable = false)
    private Tag tagID;

    public ActivityTag(Activity activity, Tag tagID) {
        this.activity = activity;
        this.tagID = tagID;
    }

    protected ActivityTag() {

    }
}