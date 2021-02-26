package org.poolc.api.project.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import org.poolc.api.project.domain.Project;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectResponse {
    private final Long id;
    private final String name;
    private final String body;
    private final String description;
    private final String genre;
    private final String duration;
    private final String thumbnailURL;
    private final List<String> memberUUIDs;

    @JsonCreator
    public ProjectResponse(Long id, String name, String body, String description, String genre, String duration, String thumbnailURL, List<String> memberUUIDs) {
        this.id = id;
        this.name = name;
        this.body = body;
        this.description = description;
        this.genre = genre;
        this.duration = duration;
        this.thumbnailURL = thumbnailURL;
        this.memberUUIDs = memberUUIDs;
    }

    public static ProjectResponse of(Project project) {
        return new ProjectResponse(project.getId(), project.getName(), project.getBody(), project.getDescription(),
                project.getGenre(), project.getDuration(), project.getThumbnailURL(), project.getMemberUUIDs());
    }
}