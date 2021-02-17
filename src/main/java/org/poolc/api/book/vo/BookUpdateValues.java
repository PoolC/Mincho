package org.poolc.api.book.vo;

import lombok.Getter;
import org.poolc.api.book.dto.BookUpdateRequest;

@Getter
public class BookUpdateValues {

    private final String title;
    private final String author;
    private final String imageURL;
    private final String info;

    public BookUpdateValues(BookUpdateRequest request) {
        this.title = request.getTitle();
        this.author = request.getAuthor();
        this.imageURL = request.getImageURL();
        this.info = request.getInfo();
    }
}
