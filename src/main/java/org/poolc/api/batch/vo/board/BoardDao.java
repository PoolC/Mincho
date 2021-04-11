package org.poolc.api.batch.vo.board;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Builder
public class BoardDao {
    private Long id;
    private String name;
    private String url_path;
    private String read_permission;
    private String write_permission;
    private Timestamp created_at;
    private Timestamp updated_at;

    protected BoardDao() {
    }

    public BoardDao(Long id, String name, String url_path, String read_permission, String write_permission, Timestamp created_at, Timestamp updated_at) {
        this.id = id;
        this.name = name;
        this.url_path = url_path;
        this.read_permission = read_permission;
        this.write_permission = write_permission;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public static BoardDao of(Boards value) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return BoardDao.builder()
                .id(value.getId())
                .name(value.getName())
                .url_path(value.getUrlPath())
                .read_permission(value.getReadPermission())
                .write_permission(value.getWritePermission())
                .created_at(Timestamp.valueOf(LocalDateTime.parse(value.getCreatedAt().substring(0, 19), formatter)))
                .updated_at(Timestamp.valueOf(LocalDateTime.parse(value.getUpdatedAt().substring(0, 19), formatter)))
                .build();
    }
}
