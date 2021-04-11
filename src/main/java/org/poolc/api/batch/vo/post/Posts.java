package org.poolc.api.batch.vo.post;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Posts {
    private Long id;
    private Long board_id;
    private String author_uuid;
    private String title;
    private String body;
    private Long vote_id;
    private String created_at;
    private String updated_at;

    public Posts() {
    }

    public Posts(Long id, Long board_id, String author_uuid, String title, String body, Long vote_id, String created_at, String updated_at) {
        this.id = id;
        this.board_id = board_id;
        this.author_uuid = author_uuid;
        this.title = title;
        this.body = body;
        this.vote_id = vote_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

}
