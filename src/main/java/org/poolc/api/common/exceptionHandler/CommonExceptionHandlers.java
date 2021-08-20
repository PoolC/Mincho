package org.poolc.api.common.exceptionHandler;

import org.poolc.api.auth.exception.UnactivatedException;
import org.poolc.api.auth.exception.UnauthenticatedException;
import org.poolc.api.auth.exception.UnauthorizedException;
import org.poolc.api.common.exception.ConflictException;
import org.poolc.api.member.exception.WrongPasswordException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.naming.NoPermissionException;
import java.util.Collections;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
public class CommonExceptionHandlers {
    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<Map<String, String>> badRequestHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Collections.singletonMap("message", e.getMessage()));
    }

    @ExceptionHandler({UnauthenticatedException.class, WrongPasswordException.class})
    public ResponseEntity<Map<String, String>> unauthorizedHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Collections.singletonMap("message", e.getMessage()));
    }

    @ExceptionHandler({UnactivatedException.class, UnauthorizedException.class})
    public ResponseEntity<Map<String, String>> forbiddenHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Collections.singletonMap("message", e.getMessage()));
    }

    @ExceptionHandler({NoSuchElementException.class})
    public ResponseEntity<Map<String, String>> notFoundHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap("message", e.getMessage()));
    }

    @ExceptionHandler({ConflictException.class, IndexOutOfBoundsException.class, NoPermissionException.class})
    public ResponseEntity<Map<String, String>> conflictHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Collections.singletonMap("message", e.getMessage()));
    }
}
