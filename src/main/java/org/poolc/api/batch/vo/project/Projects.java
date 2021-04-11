package org.poolc.api.batch.vo.project;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Projects {
    private Long id;
    private String name;
    private String genre;
    private String thumbnail_url;
    private String body;
    private String created_at;
    private String updated_at;
    private String participants;
    private String duration;
    private String description;

    public Projects() {
    }

    public Projects(Long id, String name, String genre, String thumbnail_url, String body, String created_at, String updated_at, String participants, String duration, String description) {
        this.id = id;
        this.name = name;
        this.genre = genre;
        this.thumbnail_url = thumbnail_url;
        this.body = body;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.participants = participants;
        this.duration = duration;
        this.description = description;
    }
}
