package org.poolc.api.book.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import org.poolc.api.common.domain.TimestampEntity;
import org.poolc.api.member.domain.Member;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@SequenceGenerator(
        name = "BOOK_SEQ_GENERATOR",
        sequenceName = "BOOK_SEQ"
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Book extends TimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOOK_SEQ_GENERATOR")
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "borrower", referencedColumnName = "UUID")
    private Member borrower = null;

    @Column(name = "title", length = 1024, nullable = false)
    private String title;

    @Column(name = "author", length = 1024, nullable = false)
    private String author;

    @Column(name = "image_url", length = 1024)
    private String imageURL;

    @Column(name = "info", length = 1024)
    private String info;

    @Column(name = "borrow_date")
    private LocalDate borrowDate;

    @Column(name = "status", columnDefinition = "varchar(64) default 'AVAILABLE'")
    @Enumerated(EnumType.STRING)
    private BookStatus status = BookStatus.AVAILABLE;

    protected Book() {
    }

    public Book(String title, String author, String imageURL, String info, BookStatus status) {
        this.title = title;
        this.author = author;
        this.imageURL = imageURL;
        this.info = info;
        this.status = status;
    }

    public void borrowBook(Member member) {
        this.status = BookStatus.UNAVAILABLE;
        this.borrowDate = LocalDate.now();
        this.borrower = member;
    }

    public void returnBook() {
        this.status = BookStatus.AVAILABLE;
        this.borrowDate = null;
        this.borrower = null;
    }

    public void update(String title, String author, String imageURL, String info) {
        this.title = title;
        this.author = author;
        this.imageURL = imageURL;
        this.info = info;
    }
}
