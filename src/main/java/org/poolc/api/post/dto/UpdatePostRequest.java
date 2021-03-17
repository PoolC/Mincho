package org.poolc.api.post.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.List;

@Getter
public class UpdatePostRequest {
    private final String body;
    private final String title;
    private final List<String> fileList;

    @JsonCreator
    public UpdatePostRequest(String body, String title, List<String> fileList) {
        this.body = body;
        this.title = title;
        this.fileList = fileList;
    }
}
