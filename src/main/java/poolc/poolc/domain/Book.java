package poolc.poolc.domain;

import lombok.Getter;
import lombok.Setter;
import poolc.poolc.enums.BookStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Book")
@Getter
@Setter
public class Book {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long ID;

    @OneToOne
    @JoinColumn(name = "UUID")
    private Member borrower;

    @Column(name = "title", length = 1024, nullable = false)
    private String title;

    @Column(name = "author", length = 1024, nullable = false)
    private String author;

    @Column(name = "imageURL", length = 1024)
    private String imageURL;

    @Column(name = "info", length = 64)
    private String info;

    @Column(name = "status", columnDefinition = "varchar(64) default 'available'")
    @Enumerated(EnumType.STRING)
    private BookStatus status = BookStatus.available;

    @Column(name = "borrowDate")
    private LocalDateTime borrowDate;
}
