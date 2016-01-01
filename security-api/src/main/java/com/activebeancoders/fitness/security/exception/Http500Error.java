package com.activebeancoders.fitness.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Dan Barrese
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class Http500Error extends RuntimeException {

    public Http500Error() {
    }

    public Http500Error(String message) {
        super(message);
    }

    public Http500Error(String message, Throwable cause) {
        super(message, cause);
    }

    public Http500Error(Throwable cause) {
        super(cause);
    }

}

