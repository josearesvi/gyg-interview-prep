package com.gyg.prep.experiences;

import com.gyg.prep.experiences.activity.Activity;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;

public final class TestData {

    private TestData() {
    }

    public static Activity activity(String title, String city, String price, double rating, int capacity) {
        return new Activity(title, city, new BigDecimal(price), rating, capacity,
                Instant.now().plus(Duration.ofDays(10)));
    }

    public static Activity startingIn(Duration fromNow, int capacity) {
        return new Activity("Starts soon", "Rome", new BigDecimal("10.00"), 4.0, capacity, Instant.now().plus(fromNow));
    }

    public static String bookingJson(long activityId, int participants) {
        return """
                {"activityId": %d, "customerEmail": "ana@example.com", "participants": %d}""".formatted(activityId, participants);
    }
}
