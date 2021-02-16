package org.poolc.api.book.controller;

import lombok.RequiredArgsConstructor;
import org.poolc.api.book.dto.BookResponse;
import org.poolc.api.book.dto.BookWithBorrowerResponse;
import org.poolc.api.book.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.NoSuchElementException;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;

    @GetMapping(value = "/{bookID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BookWithBorrowerResponse> findOneBookWithBorrower(@PathVariable("bookID") Long id) {
        return ResponseEntity.ok().body(new BookWithBorrowerResponse(bookService.findOneBook(id)));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BookResponse>> findBooks() {
        return ResponseEntity.ok().body(bookService.findBooks().stream()
                .map(b -> new BookResponse(b))
                .collect(toList()));
    }


    @PutMapping(value = "/borrow/{bookID}")
    public ResponseEntity borrowBook(HttpServletRequest request, @PathVariable("bookID") Long id) {
        bookService.borrowBook(request.getAttribute("UUID").toString(), id);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/return/{bookID}")
    public ResponseEntity returnBook(HttpServletRequest request, @PathVariable("bookID") Long id) {
        bookService.returnBook(request.getAttribute("UUID").toString(), id);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> noSuchElementHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> illegalStateException(Exception e) {
        System.out.println(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
    }
}