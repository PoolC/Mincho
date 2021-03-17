package org.poolc.api.post.vo;

import lombok.Getter;
import org.poolc.api.post.dto.UpdatePostRequest;

import java.util.List;

@Getter
public class PostUpdateValues {
    private final String title;
    private final String body;
    private final List<String> fileList;

    public PostUpdateValues(UpdatePostRequest updatePostRequest) {
        this.title = updatePostRequest.getTitle();
        this.body = updatePostRequest.getBody();
        this.fileList = updatePostRequest.getFileList();
    }
}
