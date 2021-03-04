package org.poolc.api.comment.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class UpdateCommentRequest {
    private String body;

    @JsonCreator
    public UpdateCommentRequest(String body) {
        this.body = body;
    }
}
