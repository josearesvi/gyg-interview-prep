package com.gyg.prep.bookings.booking;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

/**
 * A PENDING booking holds its spots for HOLD while the customer pays. After that it must be dropped from the
 * in-memory list of held bookings so the spots are released.
 */
@Component
public class PendingBookingSweeper {

    static final Duration HOLD = Duration.ofMinutes(15);

    /** Removes expired PENDING bookings from {@code held} and returns how many were removed. */
    public int sweep(List<Booking> held, Instant now) {
        int removed = 0;
        for (Booking booking : held) {
            if (booking.getStatus() == BookingStatus.PENDING && booking.getCreatedAt().plus(HOLD).isBefore(now)) {
                held.remove(booking);
                removed++;
            }
        }
        return removed;
    }
}
