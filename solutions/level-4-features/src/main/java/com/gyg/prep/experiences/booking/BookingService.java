package com.gyg.prep.experiences.booking;

import com.gyg.prep.experiences.activity.Activity;
import com.gyg.prep.experiences.activity.ActivityRepository;
import com.gyg.prep.experiences.common.ConflictException;
import com.gyg.prep.experiences.common.NotFoundException;
import com.gyg.prep.experiences.common.UnprocessableException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Service
public class BookingService {

    static final Duration FREE_CANCELLATION_WINDOW = Duration.ofHours(24);

    private final ActivityRepository activities;
    private final BookingRepository bookings;
    private final IdempotencyRecordRepository idempotencyRecords;
    private final Clock clock;

    public BookingService(ActivityRepository activities, BookingRepository bookings,
                          IdempotencyRecordRepository idempotencyRecords, Clock clock) {
        this.activities = activities;
        this.bookings = bookings;
        this.idempotencyRecords = idempotencyRecords;
        this.clock = clock;
    }

    /** Kept for callers without an idempotency key. */
    @Transactional
    public Booking book(long activityId, String customerEmail, int participants) {
        return book(activityId, customerEmail, participants, null);
    }

    @Transactional
    public Booking book(long activityId, String customerEmail, int participants, String idempotencyKey) {
        String fingerprint = activityId + "|" + customerEmail.toLowerCase() + "|" + participants;

        // FEATURE 2: replay the original result instead of booking (and charging) twice.
        if (idempotencyKey != null) {
            Optional<IdempotencyRecord> previous = idempotencyRecords.findById(idempotencyKey);
            if (previous.isPresent()) {
                if (!previous.get().getRequestFingerprint().equals(fingerprint)) {
                    throw new UnprocessableException("Idempotency-Key was already used for a different request");
                }
                return bookings.findById(previous.get().getBookingId()).orElseThrow();
            }
        }

        // FEATURE 3: lock the activity row first; the capacity check below is now race-free.
        Activity activity = activities.findByIdForUpdate(activityId)
                .orElseThrow(() -> new NotFoundException("activity " + activityId + " not found"));

        int booked = bookings.countBookedSeats(activityId);
        if (booked + participants > activity.getCapacity()) {
            throw new ConflictException("not enough spots left");
        }

        BigDecimal total = activity.getPrice().multiply(BigDecimal.valueOf(participants));
        Booking booking = bookings.save(new Booking(activity, customerEmail, participants, total, Instant.now(clock)));

        if (idempotencyKey != null) {
            idempotencyRecords.save(new IdempotencyRecord(idempotencyKey, fingerprint, booking.getId(), Instant.now(clock)));
        }
        return booking;
    }

    /** FEATURE 4: free cancellation until 24 hours before the start. */
    @Transactional
    public Booking cancel(long bookingId) {
        Booking booking = bookings.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("booking " + bookingId + " not found"));
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new ConflictException("booking " + bookingId + " is already cancelled");
        }
        Instant deadline = booking.getActivity().getStartsAt().minus(FREE_CANCELLATION_WINDOW);
        if (Instant.now(clock).isAfter(deadline)) {
            throw new ConflictException("free cancellation ends 24 hours before the activity starts");
        }
        booking.cancel();
        return booking;
    }
}
