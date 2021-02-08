package poolc.poolc.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import poolc.poolc.domain.Book;
import poolc.poolc.domain.Member;
import poolc.poolc.enums.BookStatus;
import poolc.poolc.service.BookService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
public class BookApiController {

    private final BookService bookService;

    @GetMapping("/api/book")
    public List<BookDto> findBooks() {
        List<BookDto> collect = bookService.findBooks().stream()
                .map(b -> new BookDto(b))
                .collect(toList());
        return collect;
    }

    @GetMapping("/api/book/{bookID}")
    public BookWithBorrowerDto findOneBookWithBorrower(@PathVariable("bookID") Long id) {
        Optional<Book> book = bookService.findOneBook(id);
        if (book.equals(Optional.empty())) {
            throw new ResourceNotFoundException();
        }
        return new BookWithBorrowerDto(book.get());
    }

    @PutMapping("/api/book/borrow/{bookID}")
    public ResponseEntity borrowBook(@PathVariable("bookID") Long id,
                                     @RequestBody BorrowRequest request) {
        try {
            bookService.borrowBook(request.id, id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("성공적으로 대여하였습니다");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @PutMapping("/api/book/return/{bookID}")
    public ResponseEntity returnBook(@PathVariable("bookID") Long id,
                                     @RequestBody ReturnRequest request) {
        try {
            bookService.returnBook(request.id, id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("성공적으로 반납하였습니다");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @Data
    static class BorrowRequest {
        private String id;
    }


    @Data
    static class ReturnRequest {
        private String id;
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "존재하지 않는 책입니다.")
    public class ResourceNotFoundException extends RuntimeException {
    }

    @Data
    static class BookDto {
        Long id;
        String title;
        String author;
        String imageURL;
        BookStatus status;

        public BookDto(Book book) {
            this.id = book.getID();
            this.title = book.getTitle();
            this.author = book.getAuthor();
            this.imageURL = book.getImageURL();
            this.status = book.getStatus();
        }
    }

    @Data
    static class BookWithBorrowerDto {
        Long id;
        String title;
        String author;
        String imageURL;
        BookStatus status;
        String info;
        LocalDateTime borrowDate;
        BorrowerDto borrower;

        public BookWithBorrowerDto(Book book) {
            this.id = book.getID();
            this.title = book.getTitle();
            this.author = book.getAuthor();
            this.imageURL = book.getImageURL();
            this.status = book.getStatus();
            this.info = book.getInfo();
            this.borrowDate = book.getBorrowDate();
            if (book.getBorrower() == null) {
                this.borrower = null;
            } else {
                this.borrower = new BorrowerDto(book.getBorrower());
            }
        }
    }

    @Data
    static class BorrowerDto {
        String id;
        String name;

        public BorrowerDto(Member member) {
            this.id = member.getUUID();
            this.name = member.getName();
        }
    }
}
