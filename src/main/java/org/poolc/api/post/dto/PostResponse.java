package org.poolc.api.post.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import org.poolc.api.post.domain.Comment;
import org.poolc.api.post.domain.Post;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PostResponse {
    private final Long postId;
    private final String memberUuid;
    private final String memberName;
    private final String title;
    private final String body;
    private final LocalDateTime createdAt;
    
    private final List<Comment> comments;
    private final Long commentCount;

    @JsonCreator
    public PostResponse(Long postId, String memberUuid, String memberName, String title, String body, LocalDateTime createdAt, List<Comment> comments, Long commentCount) {
        this.postId = postId;
        this.memberUuid = memberUuid;
        this.memberName = memberName;
        this.title = title;
        this.body = body;
        this.createdAt = createdAt;
        this.comments = comments;
        this.commentCount = commentCount;
    }

    public PostResponse(Post post) {
        this.postId = post.getId();
        this.memberUuid = post.getMember().getUUID();
        this.memberName = post.getMember().getName();
        this.title = post.getTitle();
        this.body = post.getBody();
        this.createdAt = post.getCreatedAt();
        this.comments = post.getCommentList();
        this.commentCount = (long) this.comments.size();
    }
}
