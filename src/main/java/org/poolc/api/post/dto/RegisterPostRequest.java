package org.poolc.api.post.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class RegisterPostRequest {
    private final Long boardId;
    private final String title;
    private final String body;

    @JsonCreator
    public RegisterPostRequest(Long boardId, String title, String body) {
        this.boardId = boardId;
        this.title = title;
        this.body = body;
    }
}
