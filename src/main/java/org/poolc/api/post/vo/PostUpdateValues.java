package org.poolc.api.post.vo;

import lombok.Getter;
import org.poolc.api.post.dto.UpdatePostRequest;

@Getter
public class PostUpdateValues {
    private final String title;
    private final String body;

    public PostUpdateValues(UpdatePostRequest updatePostRequest) {
        this.title = updatePostRequest.getTitle();
        this.body = updatePostRequest.getBody();
    }
}
