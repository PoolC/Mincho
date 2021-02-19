package org.poolc.api.book.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import org.poolc.api.book.domain.Book;
import org.poolc.api.enums.BookStatus;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookResponse {
    private final Long id;
    private final String title;
    private final String author;
    private final String imageURL;
    private final BookStatus status;

    @JsonCreator
    public BookResponse(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.imageURL = book.getImageURL();
        this.status = book.getStatus();
    }
}
