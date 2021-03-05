package org.poolc.api.board.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.Map;

@ControllerAdvice("org.poolc.api.board")
public class BoardExceptionHandler {
    @ExceptionHandler(DuplicateBoardException.class)
    public ResponseEntity<Map<String, String>> boardAlreadyExistException(Exception e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap("message", e.getMessage()));
    }
}
