package com.gyg.prep.bookings;

import com.gyg.prep.bookings.activity.Activity;
import com.gyg.prep.bookings.activity.ActivityRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Demo data for `mvn spring-boot:run`. Tests run with the "test" profile and create their own data. */
@Component
@Profile("!test")
class SeedData implements CommandLineRunner {

    private final ActivityRepository activities;

    SeedData(ActivityRepository activities) {
        this.activities = activities;
    }

    @Override
    public void run(String... args) {
        activities.save(new Activity("Colosseum skip-the-line tour", "Rome", new BigDecimal("49.90"), "EUR", 20,
                LocalDate.of(2026, 4, 1), LocalDate.of(2026, 10, 31)));
        activities.save(new Activity("Berlin Wall bike tour", "Berlin", new BigDecimal("29.00"), "EUR", 12,
                LocalDate.of(2026, 3, 1), LocalDate.of(2026, 11, 30)));
        activities.save(new Activity("Sagrada Familia guided visit", "Barcelona", new BigDecimal("39.50"), "EUR", 2,
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 12, 31)));
    }
}
