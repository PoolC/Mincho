package poolc.poolc.domain;

import lombok.Getter;
import lombok.Setter;
import poolc.poolc.enums.BookStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

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
    private Member borrower = null;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(getID(), book.getID()) &&
                Objects.equals(getBorrower(), book.getBorrower()) &&
                Objects.equals(getTitle(), book.getTitle()) &&
                Objects.equals(getAuthor(), book.getAuthor()) &&
                Objects.equals(getImageURL(), book.getImageURL()) &&
                Objects.equals(getInfo(), book.getInfo()) &&
                getStatus() == book.getStatus() &&
                Objects.equals(getBorrowDate(), book.getBorrowDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getID(), getBorrower(), getTitle(), getAuthor(), getImageURL(), getInfo(), getStatus(), getBorrowDate());
    }
}
