package org.poolc.api.comment.vo;

import lombok.Getter;
import org.poolc.api.comment.dto.UpdateCommentRequest;

@Getter
public class CommentUpdateValues {
    private final String body;

    public CommentUpdateValues(UpdateCommentRequest request) {
        this.body = request.getBody();
    }
}
