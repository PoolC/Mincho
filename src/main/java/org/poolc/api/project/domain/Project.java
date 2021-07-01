package org.poolc.api.project.domain;

import lombok.Getter;
import org.poolc.api.common.domain.TimestampEntity;
import org.poolc.api.project.vo.ProjectUpdateValues;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SequenceGenerator(
        name = "PROJECT_SEQ_GENERATOR",
        sequenceName = "PROJECT_SEQ",
        allocationSize = 1
)
@Table(name = "Project")
public class Project extends TimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROJECT_SEQ_GENERATOR")
    @Column(name = "id")
    private long id;

    @Column(name = "name", nullable = false, columnDefinition = "varchar(255)")
    private String name;

    @Column(name = "description", nullable = false, columnDefinition = "text")
    private String description;

    @Column(name = "genre", nullable = false, columnDefinition = "varchar(255)")
    private String genre;

    @Column(name = "duration", nullable = false, columnDefinition = "varchar(255)")
    private String duration;

    @Column(name = "thumbnail_url", nullable = false, columnDefinition = "varchar(255)")
    private String thumbnailURL;

    @Column(name = "body", nullable = false, columnDefinition = "text")
    private String body;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "project_members", joinColumns = @JoinColumn(name = "project_id"))
    private List<String> memberLoginIDs = new ArrayList<>();

    protected Project() {
    }

    public Project(String name, String description, String genre, String duration, String thumbnailURL, String body) {
        this.name = name;
        this.description = description;
        this.genre = genre;
        this.duration = duration;
        this.thumbnailURL = thumbnailURL;
        this.body = body;
    }

    public void setMembers(List<String> members) {
        this.memberLoginIDs = members;
    }

    public void update(ProjectUpdateValues projectUpdateValues) {
        this.name = projectUpdateValues.getName();
        this.description = projectUpdateValues.getDescription();
        this.genre = projectUpdateValues.getGenre();
        this.duration = projectUpdateValues.getDuration();
        this.thumbnailURL = projectUpdateValues.getThumbnailURL();
        this.body = projectUpdateValues.getBody();
        this.memberLoginIDs.clear();
        this.memberLoginIDs.addAll(projectUpdateValues.getMemberLoginIDs());
    }
}
