package com.gyg.prep.experiences.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("""
            select coalesce(sum(b.participants), 0) from Booking b
            where b.activity.id = :activityId and b.status = com.gyg.prep.experiences.booking.BookingStatus.CONFIRMED""")
    int countBookedSeats(Long activityId);
}
