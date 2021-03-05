package org.poolc.api.post.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Getter;
import org.poolc.api.comment.dto.CommentResponse;
import org.poolc.api.post.domain.Post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class PostResponse {
    private final Long postId;
    private final String writerLoginId;
    private final String writerName;
    private final String title;
    private final String body;
    private final LocalDateTime createdAt;

    private final List<CommentResponse> comments;
    private final Long commentCount;

    @JsonCreator
    public PostResponse(Long postId, String writerLoginId, String writerName, String title, String body, LocalDateTime createdAt, List<CommentResponse> comments, Long commentCount) {
        this.postId = postId;
        this.writerLoginId = writerLoginId;
        this.writerName = writerName;
        this.title = title;
        this.body = body;
        this.createdAt = createdAt;
        this.comments = comments;
        this.commentCount = commentCount;
    }

    public static PostResponse of(Post post) {
        return PostResponse.builder()
                .postId(post.getId())
                .writerLoginId(post.getMember().getLoginID())
                .writerName(post.getMember().getName())
                .title(post.getTitle())
                .body(post.getBody())
                .createdAt(post.getCreatedAt())
                .comments(post.getCommentList()
                        .stream().map(CommentResponse::of)
                        .collect(Collectors.toList()))
                .commentCount((long) post.getCommentList().size())
                .build();
    }

}
