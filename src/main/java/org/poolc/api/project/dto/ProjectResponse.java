package org.poolc.api.project.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import org.poolc.api.project.domain.Project;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectResponse {
    private final Long id;
    private final String name;
    private final String description;
    private final String genre;
    private final String duration;
    private final String thumbnailURL;

    @JsonCreator
    public ProjectResponse(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.description = project.getDescription();
        this.genre = project.getGenre();
        this.duration = project.getDuration();
        this.thumbnailURL = project.getThumbnailURL();
    }
}