package com.activebeancoders.fitness.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Dan Barrese
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class Http401Error extends RuntimeException {

    public Http401Error() {
    }

    public Http401Error(String message) {
        super(message);
    }

    public Http401Error(String message, Throwable cause) {
        super(message, cause);
    }

    public Http401Error(Throwable cause) {
        super(cause);
    }

}

