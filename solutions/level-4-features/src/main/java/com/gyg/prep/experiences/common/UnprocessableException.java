package com.gyg.prep.experiences.common;

/** Well-formed, but semantically unacceptable, e.g. an Idempotency-Key reused for a different request. */
public class UnprocessableException extends RuntimeException {
    public UnprocessableException(String message) {
        super(message);
    }
}
