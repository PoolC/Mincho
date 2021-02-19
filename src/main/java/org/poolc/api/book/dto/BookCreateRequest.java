package org.poolc.api.book.dto;

import lombok.Getter;

@Getter
public class BookCreateRequest {

    private final String title;
    private final String author;
    private final String imageURL;
    private final String info;

    public BookCreateRequest(String title, String author, String imageURL, String info) {
        this.title = title;
        this.author = author;
        this.imageURL = imageURL;
        this.info = info;
    }
}
