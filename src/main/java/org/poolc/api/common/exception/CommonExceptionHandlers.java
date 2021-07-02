package org.poolc.api.common.exception;

import org.poolc.api.auth.exception.UnactivatedException;
import org.poolc.api.auth.exception.UnauthenticatedException;
import org.poolc.api.auth.exception.UnauthorizedException;
import org.poolc.api.member.exception.WrongPasswordException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
public class CommonExceptionHandlers {
    @ExceptionHandler({UnactivatedException.class, UnauthorizedException.class, WrongPasswordException.class})
    public ResponseEntity<Map<String, String>> unactivatedMemberHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Collections.singletonMap("message", e.getMessage()));
    }

    @ExceptionHandler(UnauthenticatedException.class)
    public ResponseEntity<Map<String, String>> unauthenticatedMemberHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Collections.singletonMap("message", e.getMessage()));
    }

    @ExceptionHandler({NoSuchElementException.class, IllegalArgumentException.class, IllegalStateException.class, IndexOutOfBoundsException.class})
    public ResponseEntity<Map<String, String>> noSuchElementHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap("message", e.getMessage()));
    }
}
