package org.poolc.api.book.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import org.poolc.api.book.domain.Book;
import org.poolc.api.book.domain.BookStatus;
import org.poolc.api.member.dto.MemberResponse;

import java.time.LocalDate;
import java.util.Optional;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookResponse {
    private final Long id;
    private final String title;
    private final String author;
    private final String imageURL;
    private final BookStatus status;
    private final String info;
    private final MemberResponse borrower;
    private final LocalDate borrowDate;

    @JsonCreator

    public BookResponse(Long id, String title, String author, String imageURL, BookStatus status, String info, MemberResponse borrower, LocalDate borrowDate) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.imageURL = imageURL;
        this.status = status;
        this.info = info;
        this.borrower = borrower;
        this.borrowDate = borrowDate;
    }

    public static BookResponse of(Book book) {
        MemberResponse memberResponse = Optional.ofNullable(book.getBorrower())
                .map(MemberResponse::of)
                .orElse(null);

        return new BookResponse(book.getId(), book.getTitle(), book.getAuthor(), book.getImageURL(), book.getStatus(),
                book.getInfo(), memberResponse, book.getBorrowDate());
    }
}
