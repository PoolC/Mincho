package org.poolc.api.auth.exception;

public class UnactivatedException extends RuntimeException {
    public UnactivatedException(String message) {
        super(message);
    }
}
