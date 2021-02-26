package org.poolc.api.comment.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import org.poolc.api.comment.domain.Comment;

@Getter
public class CommentResponse {
    private final Long postId;
    private final String uuid;
    private final String memberName;
    private final String body;

    @JsonCreator
    public CommentResponse(Long postId, String uuid, String memberName, String body) {
        this.postId = postId;
        this.uuid = uuid;
        this.memberName = memberName;
        this.body = body;
    }

    public CommentResponse(Comment comment) {
        this.postId = comment.getPost().getId();
        this.uuid = comment.getMember().getUUID();
        this.memberName = comment.getMember().getName();
        this.body = comment.getBody();
    }

}
