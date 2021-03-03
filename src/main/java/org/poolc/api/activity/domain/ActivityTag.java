package org.poolc.api.activity.domain;

import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "ActivityTag")
@Getter
public class ActivityTag {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "activity_id", referencedColumnName = "id", nullable = false)
    private Activity activity;

    private String content;

    public ActivityTag(Activity activity, String content) {
        this.activity = activity;
        this.content = content;
    }

    protected ActivityTag() {

    }
}