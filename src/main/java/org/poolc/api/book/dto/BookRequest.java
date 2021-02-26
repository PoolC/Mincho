package org.poolc.api.book.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class BookRequest {

    private final String title;
    private final String author;
    private final String imageURL;
    private final String info;

    @JsonCreator
    public BookRequest(String title, String author, String imageURL, String info) {
        this.title = title;
        this.author = author;
        this.imageURL = imageURL;
        this.info = info;
    }
}
