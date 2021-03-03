package org.poolc.api.activity.exception;

import org.poolc.api.auth.exception.UnauthenticatedException;

public class NotHostException extends UnauthenticatedException {

    public NotHostException(String message) {
        super(message);
    }

}
