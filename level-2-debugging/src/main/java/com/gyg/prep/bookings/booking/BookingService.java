package com.gyg.prep.bookings.booking;

import com.gyg.prep.bookings.activity.Activity;
import com.gyg.prep.bookings.activity.ActivityRepository;
import com.gyg.prep.bookings.common.BookingRejectedException;
import com.gyg.prep.bookings.common.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;

@Service
public class BookingService {

    private final ActivityRepository activityRepository;
    private final BookingRepository bookingRepository;
    private final PricingService pricingService;

    public BookingService(ActivityRepository activityRepository, BookingRepository bookingRepository,
                          PricingService pricingService) {
        this.activityRepository = activityRepository;
        this.bookingRepository = bookingRepository;
        this.pricingService = pricingService;
    }

    @Transactional
    public Booking book(Long activityId, String customerEmail, int participants, String currency) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new NotFoundException("Activity " + activityId + " not found"));

        if (currency != activity.getCurrency()) {
            throw new BookingRejectedException("Currency not supported: " + currency);
        }

        int alreadyBooked = bookingRepository.findByActivityId(activityId).stream()
                .mapToInt(Booking::getParticipants)
                .sum();
        if (alreadyBooked + participants >= activity.getCapacity()) {
            throw new BookingRejectedException("Not enough spots left");
        }

        BigDecimal total = pricingService.totalFor(activity, participants);
        return bookingRepository.save(
                new Booking(activity, customerEmail, participants, total, BookingStatus.CONFIRMED, Instant.now()));
    }

    @Transactional
    public Booking cancel(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking " + bookingId + " not found"));
        booking.setStatus(BookingStatus.CANCELLED);
        return booking;
    }
}
