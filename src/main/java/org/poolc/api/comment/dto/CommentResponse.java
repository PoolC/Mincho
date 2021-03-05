package org.poolc.api.comment.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Getter;
import org.poolc.api.comment.domain.Comment;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentResponse {
    private final Long commentId;
    private final Long postId;
    private final String writerLoginId;
    private final String writerName;
    private final String body;
    private final LocalDateTime createdAt;

    @JsonCreator
    public CommentResponse(Long commentId, Long postId, String loginId, String memberName, String body, LocalDateTime createdAt) {
        this.commentId = commentId;
        this.postId = postId;
        this.writerLoginId = loginId;
        this.writerName = memberName;
        this.body = body;
        this.createdAt = createdAt;
    }


    public static CommentResponse of(Comment comment) {
        return CommentResponse.builder()
                .commentId(comment.getId())
                .postId(comment.getPost().getId())
                .writerLoginId(comment.getMember().getLoginID())
                .writerName(comment.getMember().getName())
                .body(comment.getBody())
                .createdAt(comment.getCreatedAt())
                .build();
    }

}
