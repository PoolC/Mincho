package org.poolc.api.project.domain;

import lombok.Getter;
import org.poolc.api.common.domain.TimestampEntity;
import org.poolc.api.project.vo.ProjectUpdateValues;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "Project")
public class Project extends TimestampEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

    @Column(name = "name", columnDefinition = "varchar(255)", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "text", nullable = false)
    private String description;

    @Column(name = "genre", columnDefinition = "varchar(255)", nullable = false)
    private String genre;

    @Column(name = "duration", columnDefinition = "varchar(255)", nullable = false)
    private String duration;

    @Column(name = "thumbnail_url", columnDefinition = "varchar(255)", nullable = false)
    private String thumbnailURL;

    @Column(name = "body", columnDefinition = "text", nullable = false)
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
