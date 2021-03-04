package org.poolc.api.post.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import org.poolc.api.comment.dto.CommentResponse;
import org.poolc.api.post.domain.Post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PostResponse {
    private final Long postId;
    private final String memberUuid;
    private final String memberName;
    private final String title;
    private final String body;
    private final LocalDateTime createdAt;

    private final List<CommentResponse> comments;
    private final Long commentCount;

    @JsonCreator
    public PostResponse(Long postId, String memberUuid, String memberName, String title, String body, LocalDateTime createdAt, List<CommentResponse> comments, Long commentCount) {
        this.postId = postId;
        this.memberUuid = memberUuid;
        this.memberName = memberName;
        this.title = title;
        this.body = body;
        this.createdAt = createdAt;
        this.comments = comments;
        this.commentCount = commentCount;
    }

    public static PostResponse of(Post post) {
        return new PostResponse(post.getId(), post.getMember().getUUID(),
                post.getMember().getName(), post.getTitle(), post.getBody(),
                post.getCreatedAt(),
                post.getCommentList().stream()
                        .map(CommentResponse::of).collect(Collectors.toList()),
                (long) post.getCommentList().size());
    }

}
