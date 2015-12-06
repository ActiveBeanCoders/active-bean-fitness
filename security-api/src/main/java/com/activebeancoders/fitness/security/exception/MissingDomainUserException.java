package com.activebeancoders.fitness.security.exception;

/**
 * @author Dan Barrese
 */
public class MissingDomainUserException extends RuntimeException {

    public MissingDomainUserException() {
    }

    public MissingDomainUserException(String message) {
        super(message);
    }

    public MissingDomainUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissingDomainUserException(Throwable cause) {
        super(cause);
    }
}
