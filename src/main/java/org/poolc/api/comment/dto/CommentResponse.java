package org.poolc.api.comment.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import org.poolc.api.comment.domain.Comment;

@Getter
public class CommentResponse {
    private final Long commentId;
    private final Long postId;
    private final String uuid;
    private final String memberName;
    private final String body;

    @JsonCreator
    public CommentResponse(Long commentId, Long postId, String uuid, String memberName, String body) {
        this.commentId = commentId;
        this.postId = postId;
        this.uuid = uuid;
        this.memberName = memberName;
        this.body = body;
    }
    
    public static CommentResponse of(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getPost().getId(),
                comment.getMember().getUUID(),
                comment.getMember().getName(),
                comment.getBody()
        );
    }

}
