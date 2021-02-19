package org.poolc.api.project.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import org.poolc.api.project.domain.Project;
import org.poolc.api.project.vo.ProjectMemberValues;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProjectWithMemberResponse {

    private final Long id;
    private final String name;
    private final String genre;
    private final String duration;
    private final String thumbnailURL;
    private final String body;
    List<ProjectMemberValues> members;

    @JsonCreator
    public ProjectWithMemberResponse(Project project) {
        this.id = project.getId();
        this.name = project.getName();
        this.genre = project.getGenre();
        this.duration = project.getDuration();
        this.thumbnailURL = project.getThumbnailURL();
        this.body = project.getBody();
        this.members = project.getMembers().stream()
                .map(m -> new ProjectMemberValues(m.getMember()))
                .collect(Collectors.toList());
    }
}
