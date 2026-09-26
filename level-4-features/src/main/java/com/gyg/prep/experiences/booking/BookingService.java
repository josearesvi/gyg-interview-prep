package com.gyg.prep.experiences.booking;

import com.gyg.prep.experiences.activity.Activity;
import com.gyg.prep.experiences.activity.ActivityRepository;
import com.gyg.prep.experiences.common.ConflictException;
import com.gyg.prep.experiences.common.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;

@Service
public class BookingService {

    private final ActivityRepository activities;
    private final BookingRepository bookings;

    public BookingService(ActivityRepository activities, BookingRepository bookings) {
        this.activities = activities;
        this.bookings = bookings;
    }

    @Transactional
    public Booking book(long activityId, String customerEmail, int participants) {
        Activity activity = activities.findById(activityId)
                .orElseThrow(() -> new NotFoundException("activity " + activityId + " not found"));

        int booked = bookings.countBookedSeats(activityId);
        if (booked + participants > activity.getCapacity()) {
            throw new ConflictException("not enough spots left");
        }

        BigDecimal total = activity.getPrice().multiply(BigDecimal.valueOf(participants));
        return bookings.save(new Booking(activity, customerEmail, participants, total, Instant.now()));
    }

    /** Feature 4 adds the cancellation policy here. */
    @Transactional
    public Booking cancel(long bookingId) {
        Booking booking = bookings.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("booking " + bookingId + " not found"));
        booking.cancel();
        return booking;
    }
}
