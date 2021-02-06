package poolc.poolc.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProjectMember that = (ProjectMember) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getProject(), that.getProject()) &&
                Objects.equals(getMember(), that.getMember());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getProject(), getMember());
    }
}
