package org.poolc.api.book.controller;

import lombok.RequiredArgsConstructor;
import org.poolc.api.book.dto.BookRequest;
import org.poolc.api.book.dto.BookResponse;
import org.poolc.api.book.exception.DuplicateBookException;
import org.poolc.api.book.service.BookService;
import org.poolc.api.book.vo.BookCreateValues;
import org.poolc.api.book.vo.BookUpdateValues;
import org.poolc.api.member.domain.Member;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;

    @GetMapping(value = "/{bookID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookResponse> findOneBookWithBorrower(@PathVariable("bookID") Long id) {
        return ResponseEntity.ok().body(BookResponse.of(bookService.findOneBook(id)));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, List<BookResponse>>> findBooks() {
        HashMap<String, List<BookResponse>> responseBody = new HashMap<>();
        responseBody.put("data", bookService.findBooks().stream()
                .map(BookResponse::of)
                .collect(toList()));
        return ResponseEntity.ok().body(responseBody);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> registerBook(@RequestBody BookRequest requestBody) {
        bookService.saveBook(new BookCreateValues(requestBody));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{bookID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteBook(@PathVariable("bookID") Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/{bookID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateBook(@RequestBody BookRequest requestBody, @PathVariable("bookID") Long id) {
        bookService.updateBook(id, new BookUpdateValues(requestBody));
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/borrow/{bookID}")
    public ResponseEntity borrowBook(@AuthenticationPrincipal Member member, @PathVariable("bookID") Long id) {
        bookService.borrowBook(member, id);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/return/{bookID}")
    public ResponseEntity returnBook(@AuthenticationPrincipal Member member, @PathVariable("bookID") Long id) {
        bookService.returnBook(member, id);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> noSuchElementHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> illegalStateException(Exception e) {
        System.out.println(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
    }

    @ExceptionHandler(DuplicateBookException.class)
    public ResponseEntity<String> runTimeHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
}