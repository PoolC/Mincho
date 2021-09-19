package org.poolc.api.post.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.List;

@Getter
public class PostsResponse {
    private final List<PostResponse> data;

    @JsonCreator
    public PostsResponse(List<PostResponse> data) {
        this.data = data;
    }

    public static PostsResponse of(List<PostResponse> posts) {
        return new PostsResponse(posts);
    }
}
