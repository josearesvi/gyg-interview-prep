package com.gyg.prep.bookings.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class BookingRejectedException extends RuntimeException {
    public BookingRejectedException(String message) {
        super(message);
    }
}
