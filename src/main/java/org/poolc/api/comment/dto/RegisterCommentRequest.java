package org.poolc.api.comment.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class RegisterCommentRequest {
    private final Long postId;
    private final String body;

    @JsonCreator
    public RegisterCommentRequest(Long postId, String body) {
        this.postId = postId;
        this.body = body;
    }
}
