package com.gyg.prep.experiences.activity;

import java.math.BigDecimal;
import java.time.Instant;

public record ActivityResponse(Long id, String title, String city, BigDecimal price, double rating, int capacity,
                               Instant startsAt) {

    public static ActivityResponse of(Activity a) {
        return new ActivityResponse(a.getId(), a.getTitle(), a.getCity(), a.getPrice(), a.getRating(),
                a.getCapacity(), a.getStartsAt());
    }
}
