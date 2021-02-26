package org.poolc.api.comment.dto;

import com.fasterxml.jackson.annotation.JsonCreator;

public class RegisterCommentRequest {
    private final Long postId;
    private final Long body;

    @JsonCreator
    public RegisterCommentRequest(Long postId, Long body) {
        this.postId = postId;
        this.body = body;
    }
}
