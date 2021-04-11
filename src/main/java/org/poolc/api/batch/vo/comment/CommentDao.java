package org.poolc.api.batch.vo.comment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Builder
public class CommentDao {
    private Long id;
    private Long post_id;
    private String author_uuid;
    private String body;
    private Timestamp created_at;
    private Timestamp updated_at;

    protected CommentDao() {

    }

    public CommentDao(Long id, Long post_id, String author_uuid, String body, Timestamp created_at, Timestamp updated_at) {
        this.id = id;
        this.post_id = post_id;
        this.author_uuid = author_uuid;
        this.body = body;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public static CommentDao of(Comments value) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return CommentDao.builder()
                .id(value.getId())
                .post_id(value.getPost_id())
                .author_uuid(value.getAuthor_uuid())
                .body(value.getBody())
                .created_at(Timestamp.valueOf(LocalDateTime.parse(value.getCreated_at().substring(0, 19), formatter)))
                .updated_at(Timestamp.valueOf(LocalDateTime.parse(value.getUpdated_at().substring(0, 19), formatter)))
                .build();
    }
}
