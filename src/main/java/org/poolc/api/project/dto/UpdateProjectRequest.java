package org.poolc.api.project.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.List;

@Getter
public class UpdateProjectRequest {

    private final String name;
    private final String description;
    private final String genre;
    private final String duration;
    private final String thumbnailURL;
    private final String body;
    List<String> memberLoginIDs;

    @JsonCreator
    public UpdateProjectRequest(String name, String description, String genre, String duration, String thumbnailURL, String body, List<String> memberLoginIDs) {
        this.name = name;
        this.description = description;
        this.genre = genre;
        this.duration = duration;
        this.thumbnailURL = thumbnailURL;
        this.body = body;
        this.memberLoginIDs = memberLoginIDs;
    }
}
