package poolc.poolc.domain;

import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "ActivityTag")
@Getter
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

}
