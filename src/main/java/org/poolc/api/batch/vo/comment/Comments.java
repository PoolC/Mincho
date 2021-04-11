package org.poolc.api.batch.vo.comment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Comments {
    private Long id;
    private Long post_id;
    private String author_uuid;
    private String body;
    private String created_at;
    private String updated_at;

    public Comments() {
    }

    public Comments(Long id, Long post_id, String author_uuid, String body, String created_at, String updated_at) {
        this.id = id;
        this.post_id = post_id;
        this.author_uuid = author_uuid;
        this.body = body;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
}
