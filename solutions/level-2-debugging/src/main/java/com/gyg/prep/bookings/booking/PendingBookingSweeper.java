package com.gyg.prep.bookings.booking;

import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

/**
 * FIXED. Bug 7: calling List.remove() inside a for-each loop. It throws ConcurrentModificationException, or, when
 * the removed element is the second-to-last one, it silently ends the loop early (hasNext() sees cursor == size).
 * Fix: removeIf, or an explicit Iterator with iterator.remove().
 */
@Component
public class PendingBookingSweeper {

    static final Duration HOLD = Duration.ofMinutes(15);

    public int sweep(List<Booking> held, Instant now) {
        int before = held.size();
        held.removeIf(b -> b.getStatus() == BookingStatus.PENDING && b.getCreatedAt().plus(HOLD).isBefore(now));
        return before - held.size();
    }
}
