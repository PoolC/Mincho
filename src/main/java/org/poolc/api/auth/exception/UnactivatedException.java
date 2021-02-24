package org.poolc.api.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UnactivatedException extends RuntimeException {
    public UnactivatedException(String message) {
        super(message);
    }
}
