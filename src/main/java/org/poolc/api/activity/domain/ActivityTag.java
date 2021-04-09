package org.poolc.api.activity.domain;

import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "ActivityTag")
@SequenceGenerator(
        name = "ACTIVITY_TAG_SEQ_GENERATOR",
        sequenceName = "ACTIVITY_TAG_SEQ"
)
@Getter
public class ActivityTag {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ACTIVITY_TAG_SEQ_GENERATOR")
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "activity_id", nullable = false, referencedColumnName = "id")
    private Activity activity;

    private String content;

    public ActivityTag(Activity activity, String content) {
        this.activity = activity;
        this.content = content;
    }

    protected ActivityTag() {

    }
}