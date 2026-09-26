package com.gyg.prep.experiences;

import com.gyg.prep.experiences.activity.ActivityRepository;
import com.gyg.prep.experiences.booking.BookingRepository;
import com.gyg.prep.experiences.booking.BookingService;
import com.gyg.prep.experiences.common.ConflictException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.RepeatedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.gyg.prep.experiences.TestData.activity;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * FEATURE 3: no overbooking under concurrent load.
 *
 * BookingService.book() is check-then-act: it reads the booked seats, then inserts. Two requests for the last
 * spot can both pass the check. Make it safe, and be ready to compare the options in the interview:
 *   pessimistic lock (SELECT ... FOR UPDATE) vs optimistic @Version + retry vs an atomic conditional UPDATE.
 *
 * Deliberately NOT @Transactional: each thread needs its own real transaction.
 */
@SpringBootTest
@ActiveProfiles("test")
class Feature3NoOverbookingTest {

    @Autowired BookingService bookingService;
    @Autowired ActivityRepository activities;
    @Autowired BookingRepository bookings;

    @AfterEach
    void cleanUp() {
        bookings.deleteAll();
        activities.deleteAll();
    }

    @RepeatedTest(3)
    void concurrentBookingsNeverExceedCapacity() throws Exception {
        long activityId = activities.save(activity("Last spots", "Rome", "10.00", 4.5, 5)).getId();
        int requests = 40;
        ExecutorService pool = Executors.newFixedThreadPool(requests);
        CountDownLatch startGun = new CountDownLatch(1);
        List<Future<Boolean>> results = new ArrayList<>();
        for (int i = 0; i < requests; i++) {
            results.add(pool.submit(() -> {
                startGun.await();
                try {
                    bookingService.book(activityId, "fan@example.com", 1);
                    return true;
                } catch (ConflictException soldOut) {
                    return false;
                }
            }));
        }
        startGun.countDown();

        int succeeded = 0;
        for (Future<Boolean> r : results) {
            if (r.get()) succeeded++;   // any other exception (deadlock, timeout) fails the test here
        }
        pool.shutdown();

        assertThat(succeeded).isEqualTo(5);
        assertThat(bookings.countBookedSeats(activityId)).isEqualTo(5);
    }
}
