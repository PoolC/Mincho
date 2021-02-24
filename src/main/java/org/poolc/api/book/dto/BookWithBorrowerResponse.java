package org.poolc.api.book.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import org.poolc.api.book.domain.Book;
import org.poolc.api.book.vo.BorrowerValues;
import org.poolc.api.book.domain.BookStatus;

import java.time.LocalDateTime;

@Getter
public class BookWithBorrowerResponse {
    private final Long id;
    private final String title;
    private final String author;
    private final String imageURL;
    private final BookStatus status;
    private final String info;
    private final LocalDateTime borrowDate;
    private final BorrowerValues borrower;

    @JsonCreator
    public BookWithBorrowerResponse(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.imageURL = book.getImageURL();
        this.status = book.getStatus();
        this.info = book.getInfo();
        this.borrowDate = book.getUpdatedAt();
        if (book.getBorrower() == null) {
            this.borrower = null;
        } else {
            this.borrower = new BorrowerValues(book.getBorrower());
        }
    }
}