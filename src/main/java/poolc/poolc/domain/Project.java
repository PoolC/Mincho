package poolc.poolc.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "Project")
public class Project {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private long id;

    @Column(name = "name", columnDefinition = "varchar(255)", nullable = false)
    private String name;

    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "genre", columnDefinition = "varchar(255)", nullable = false)
    private String genre;

    @Column(name = "duration", columnDefinition = "varchar(255)", nullable = false)
    private String duration;

    @Column(name = "thumbnailURL", columnDefinition = "varchar(255)", nullable = false)
    private String thumbnailURL;

    @Lob
    @Column(name = "body", nullable = false)
    private String body;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    List<ProjectMember> members = new ArrayList<>();

    public void addMember(ProjectMember projectMember) {
        this.members.add(projectMember);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return getId() == project.getId() &&
                Objects.equals(getName(), project.getName()) &&
                Objects.equals(getDescription(), project.getDescription()) &&
                Objects.equals(getGenre(), project.getGenre()) &&
                Objects.equals(getDuration(), project.getDuration()) &&
                Objects.equals(getThumbnailURL(), project.getThumbnailURL()) &&
                Objects.equals(getBody(), project.getBody()) &&
                Objects.equals(getCreatedAt(), project.getCreatedAt()) &&
                Objects.equals(getUpdatedAt(), project.getUpdatedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription(), getGenre(), getDuration(), getThumbnailURL(), getBody(), getCreatedAt(), getUpdatedAt(), getMembers());
    }
}
