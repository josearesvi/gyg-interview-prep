package com.getourguide.interview.error;

/** Unchecked: a checked exception can't be thrown from a lambda, and callers can't recover from it anyway. */
public class ActivityNotFoundException extends RuntimeException {
    public ActivityNotFoundException(Long id) {
        super("Activity " + id + " not found");
    }
}
