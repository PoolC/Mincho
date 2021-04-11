package org.poolc.api.batch.vo.project;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Builder
public class ProjectDao {
    private Long id;
    private Timestamp created_at;
    private Timestamp updated_at;
    private String body;
    private String description;
    private String duration;
    private String genre;
    private String name;
    private String thumbnail_url;

    public ProjectDao() {

    }

    public ProjectDao(Long id, Timestamp created_at, Timestamp updated_at, String body, String description, String duration, String genre, String name, String thumbnail_url) {
        this.id = id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.body = body;
        this.description = description;
        this.duration = duration;
        this.genre = genre;
        this.name = name;
        this.thumbnail_url = thumbnail_url;
    }

    public static ProjectDao of(Projects value) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return ProjectDao.builder()
                .id(value.getId())
                .created_at(Timestamp.valueOf(LocalDateTime.parse(value.getCreated_at().substring(0, 19), formatter)))
                .updated_at(Timestamp.valueOf(LocalDateTime.parse(value.getUpdated_at().substring(0, 19), formatter)))
                .body(value.getBody())
                .description(value.getDescription())
                .duration(value.getDuration())
                .genre(value.getGenre())
                .name(value.getName())
                .thumbnail_url(value.getThumbnail_url())
                .build();
    }

}
