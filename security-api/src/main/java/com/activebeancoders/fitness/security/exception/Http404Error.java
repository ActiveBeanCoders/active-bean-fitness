package com.activebeancoders.fitness.security.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Dan Barrese
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class Http404Error extends RuntimeException {

    public Http404Error() {
    }

    public Http404Error(String message) {
        super(message);
    }

    public Http404Error(String message, Throwable cause) {
        super(message, cause);
    }

    public Http404Error(Throwable cause) {
        super(cause);
    }

}

