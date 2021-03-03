package org.poolc.api.comment.dto;

import lombok.Getter;

@Getter
public class UpdateCommentRequest {
    private String body;

    public UpdateCommentRequest(String body) {
        this.body = body;
    }
}
