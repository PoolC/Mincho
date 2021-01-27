package poolc.poolc.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "Project")
public class Project {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private long id;

    @Column(name = "name", columnDefinition = "char(255)", nullable = false)
    private String name;

    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "genre", columnDefinition = "char(255)", nullable = false)
    private String genre;

    @Column(name = "duration", columnDefinition = "char(255)", nullable = false)
    private String duration;

    @Column(name = "thumbnailURL", columnDefinition = "char(255)", nullable = false)
    private String thumbnailURL;

    @Lob
    @Column(name = "body", nullable = false)
    private String body;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    List<ProjectMember> members = new ArrayList<>();
}
