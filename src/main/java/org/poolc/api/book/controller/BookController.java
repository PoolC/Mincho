package org.poolc.api.book.controller;

import lombok.RequiredArgsConstructor;
import org.poolc.api.auth.exception.UnauthenticatedException;
import org.poolc.api.book.dto.BookCreateRequest;
import org.poolc.api.book.dto.BookResponse;
import org.poolc.api.book.dto.BookUpdateRequest;
import org.poolc.api.book.dto.BookWithBorrowerResponse;
import org.poolc.api.book.service.BookService;
import org.poolc.api.book.vo.BookCreateValues;
import org.poolc.api.book.vo.BookUpdateValues;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    public ResponseEntity<BookWithBorrowerResponse> findOneBookWithBorrower(@PathVariable("bookID") Long id) {
        return ResponseEntity.ok().body(new BookWithBorrowerResponse(bookService.findOneBook(id)));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, List<BookResponse>>> findBooks() {
        HashMap<String, List<BookResponse>> responseBody = new HashMap<>();
        responseBody.put("data", bookService.findBooks().stream()
                .map(b -> new BookResponse(b))
                .collect(toList()));
        return ResponseEntity.ok().body(responseBody);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> registerBook(HttpServletRequest request, @RequestBody BookCreateRequest requestBody) {
        checkAdmin(request);
        bookService.saveBook(new BookCreateValues(requestBody));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{bookID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteBook(HttpServletRequest request, @PathVariable("bookID") Long id) {
        checkAdmin(request);
        bookService.deleteBook(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/{bookID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateBook(HttpServletRequest request, @RequestBody BookUpdateRequest requestBody, @PathVariable("bookID") Long id) {
        checkAdmin(request);
        bookService.updateBook(id, new BookUpdateValues(requestBody));
        return ResponseEntity.ok().build();
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
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> illegalStateException(Exception e) {
        System.out.println(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
    }

    @ExceptionHandler(UnauthenticatedException.class)
    public ResponseEntity<String> unauthenticatedHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> runTimeHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    private void checkAdmin(HttpServletRequest request) {
        if (request.getAttribute("isAdmin").equals("false")) {
            throw new UnauthenticatedException("임원진이 아닙니다");
        }
    }

}