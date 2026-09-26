package com.tourco.inventory.controller;

import com.tourco.inventory.model.Departure;

import java.time.Instant;

public record DepartureView(Long id, Long tourId, Instant startsAt, int capacity, int remaining) {

    static DepartureView of(Departure d) {
        return new DepartureView(d.getId(), d.getTour().getId(), d.getStartsAt(), d.getCapacity(), d.remaining());
    }
}
