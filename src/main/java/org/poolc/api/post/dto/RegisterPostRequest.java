package org.poolc.api.post.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class RegisterPostRequest {
    private final Long boardId;
    private final String title;
    private final String body;
    private final List<String> fileList;

    @JsonCreator
    @Builder
    public RegisterPostRequest(Long boardId, String title, String body, List<String> file_list) {
        this.boardId = boardId;
        this.title = title;
        this.body = body;
        this.fileList = file_list;
    }
}
