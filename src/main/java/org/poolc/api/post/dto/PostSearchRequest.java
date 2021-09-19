package org.poolc.api.post.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class PostSearchRequest {
    private final String title;
    private final String body;
    private final String author;

    @JsonCreator
    public PostSearchRequest(String title, String body, String author) {
        this.title = title;
        this.body = body;
        this.author = author;
    }
}
