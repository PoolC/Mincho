package org.poolc.api.post.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class UpdatePostRequest {
    private final String body;
    private final String title;

    @JsonCreator
    public UpdatePostRequest(String body, String title) {
        this.body = body;
        this.title = title;
    }
}
