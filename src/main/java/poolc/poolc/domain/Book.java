package poolc.poolc.domain;

import lombok.Getter;
import lombok.Setter;
import poolc.poolc.enums.BookStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Book {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long ID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrower", referencedColumnName = "UUID")
    private Member borrower;

    @Column(name = "title", length = 1024, nullable = false)
    private String title;

    @Column(name = "author", length = 1024, nullable = false)
    private String author;

    @Column(name = "imageURL", length = 1024)
    private String imageURL;

    @Column(name = "info", length = 64)
    private String info;

    @Column(name = "status", columnDefinition = "varchar(64) default 'AVAILABLE'")
    @Enumerated(EnumType.STRING)
    private BookStatus status = BookStatus.AVAILABLE;

    @Column(name = "borrowDate")
    private LocalDateTime borrowDate;
}
