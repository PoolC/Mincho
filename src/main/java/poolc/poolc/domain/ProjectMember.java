package poolc.poolc.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@Table(name = "ProjectMember",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"projectID", "memberUUID"})})
public class ProjectMember {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "projectID", referencedColumnName = "ID", nullable = false)
    private Project project;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "memberUUID", referencedColumnName = "UUID", nullable = false)
    private Member member;
}