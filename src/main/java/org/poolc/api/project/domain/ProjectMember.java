package org.poolc.api.project.domain;

import lombok.Getter;
import org.poolc.api.member.domain.Member;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
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

    protected ProjectMember() {

    }

    public ProjectMember(Project project, Member member) {
        this.project = project;
        this.member = member;
    }
}
