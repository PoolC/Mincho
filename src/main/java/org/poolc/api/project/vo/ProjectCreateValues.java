package org.poolc.api.project.vo;

import lombok.Getter;
import org.poolc.api.project.dto.RegisterProjectRequest;

import java.util.List;

@Getter
public class ProjectCreateValues {

    private final String name;
    private final String description;
    private final String genre;
    private final String duration;
    private final String thumbnailURL;
    private final String body;
    List<String> memberUUIDs;

    public ProjectCreateValues(RegisterProjectRequest request) {
        this.name = request.getName();
        this.description = request.getDescription();
        this.genre = request.getGenre();
        this.duration = request.getDuration();
        this.thumbnailURL = request.getThumbnailURL();
        this.body = request.getBody();
        this.memberUUIDs = request.getMembers();
    }
}
