package org.poolc.api.batch.vo.post;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Builder
public class PostDao {
    private Long id;
    private Long board_id;
    private String author_uuid;
    private String title;
    private String body;
    private Timestamp created_at;
    private Timestamp updated_at;

    protected PostDao() {
    }

    public PostDao(Long id, Long board_id, String author_uuid, String title, String body, Timestamp created_at, Timestamp updated_at) {
        this.id = id;
        this.board_id = board_id;
        this.author_uuid = author_uuid;
        this.title = title;
        this.body = body;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public static PostDao of(Posts value) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return PostDao.builder()
                .id(value.getId())
                .board_id(value.getBoard_id())
                .author_uuid(value.getAuthor_uuid())
                .title(value.getTitle())
                .body(value.getBody())
                .created_at(Timestamp.valueOf(LocalDateTime.parse(value.getCreated_at().substring(0, 19), formatter)))
                .updated_at(Timestamp.valueOf(LocalDateTime.parse(value.getUpdated_at().substring(0, 19), formatter)))
                .build();
    }
}
