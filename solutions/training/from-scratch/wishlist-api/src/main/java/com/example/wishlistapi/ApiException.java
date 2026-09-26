package com.example.wishlistapi;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/** Domain errors, each mapped to its HTTP status. */
public abstract sealed class ApiException extends RuntimeException {

    ApiException(String message) {
        super(message);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static final class NotFound extends ApiException {
        public NotFound(String message) { super(message); }
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    public static final class Duplicate extends ApiException {
        public Duplicate(String message) { super(message); }
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public static final class LimitReached extends ApiException {
        public LimitReached(String message) { super(message); }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static final class BadRequest extends ApiException {
        public BadRequest(String message) { super(message); }
    }
}
