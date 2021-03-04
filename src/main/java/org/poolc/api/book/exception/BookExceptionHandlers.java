package org.poolc.api.book.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice("org.poolc.api.book")
public class BookExceptionHandlers {
    @ExceptionHandler(DuplicateBookException.class)
    public ResponseEntity<String> runTimeHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
}
