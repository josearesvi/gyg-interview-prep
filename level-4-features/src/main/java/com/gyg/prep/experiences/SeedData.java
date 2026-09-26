package com.gyg.prep.experiences;

import com.gyg.prep.experiences.activity.Activity;
import com.gyg.prep.experiences.activity.ActivityRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;

@Component
@Profile("!test")
class SeedData implements CommandLineRunner {

    private final ActivityRepository activities;

    SeedData(ActivityRepository activities) {
        this.activities = activities;
    }

    @Override
    public void run(String... args) {
        Instant inAWeek = Instant.now().plus(Duration.ofDays(7));
        activities.save(new Activity("Colosseum skip-the-line tour", "Rome", new BigDecimal("49.90"), 4.7, 20, inAWeek));
        activities.save(new Activity("Vatican Museums early entry", "Rome", new BigDecimal("69.00"), 4.8, 15, inAWeek));
        activities.save(new Activity("Trastevere food tour", "Rome", new BigDecimal("89.00"), 4.9, 10, inAWeek));
        activities.save(new Activity("Berlin Wall bike tour", "Berlin", new BigDecimal("29.00"), 4.6, 12, inAWeek));
        activities.save(new Activity("Spree river cruise", "Berlin", new BigDecimal("19.50"), 4.3, 80, inAWeek));
    }
}
