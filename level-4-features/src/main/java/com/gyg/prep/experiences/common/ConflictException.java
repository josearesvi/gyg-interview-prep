package com.gyg.prep.experiences.common;

/** The request is valid but conflicts with the current state (sold out, too late to cancel, ...). */
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
