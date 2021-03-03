package org.poolc.api.activity.exception;

import org.poolc.api.auth.exception.UnauthenticatedException;

public class NotAdminOrHostException extends UnauthenticatedException {

    public NotAdminOrHostException(String message) {
        super(message);
    }
}
