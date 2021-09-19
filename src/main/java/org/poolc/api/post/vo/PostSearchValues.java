package org.poolc.api.post.vo;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.List;

@Getter
public class PostSearchValues {
    private final String title;
    private final String body;
    private final String author;
    private final List<String> boards;

    @JsonCreator
    public PostSearchValues(String title, String body, String author, List<String> boards) {
        this.title = title;
        this.body = body;
        this.author = author;
        this.boards = boards;
    }
}
