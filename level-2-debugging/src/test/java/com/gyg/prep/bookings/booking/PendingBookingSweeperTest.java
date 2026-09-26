package com.gyg.prep.bookings.booking;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PendingBookingSweeperTest {

    private static final Instant NOW = Instant.parse("2026-10-01T12:00:00Z");
    private final PendingBookingSweeper sweeper = new PendingBookingSweeper();

    private static Booking pending(int minutesAgo) {
        return new Booking(null, "x@example.com", 1, null, BookingStatus.PENDING, NOW.minusSeconds(60L * minutesAgo));
    }

    @Test
    void removesAllExpiredBookings() {
        Booking fresh1 = pending(1), fresh2 = pending(5);
        List<Booking> held = new ArrayList<>(List.of(pending(30), fresh1, pending(60), fresh2, pending(20)));

        assertThat(sweeper.sweep(held, NOW)).isEqualTo(3);
        assertThat(held).containsExactly(fresh1, fresh2);
    }

    @Test
    void removesTheLastTwoExpiredBookings() {
        // This case does NOT throw with the buggy code. It silently skips one. Work out why.
        Booking fresh = pending(1);
        List<Booking> held = new ArrayList<>(List.of(fresh, pending(30), pending(40)));

        assertThat(sweeper.sweep(held, NOW)).isEqualTo(2);
        assertThat(held).containsExactly(fresh);
    }

    @Test
    void confirmedBookingsAreNeverSwept() {
        Booking confirmed = new Booking(null, "x@example.com", 1, null, BookingStatus.CONFIRMED,
                NOW.minusSeconds(3600));
        List<Booking> held = new ArrayList<>(List.of(confirmed));

        assertThat(sweeper.sweep(held, NOW)).isZero();
        assertThat(held).containsExactly(confirmed);
    }
}
