package com.tourco.inventory.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.time.Instant;

/** One scheduled run of a tour, e.g. "Colosseum tour, 1 Oct 10:00". */
@Entity
public class Departure {

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Tour tour;
    private Instant startsAt;
    private int capacity;
    private int reserved;

    protected Departure() {
    }

    public Departure(Tour tour, Instant startsAt, int capacity) {
        this.tour = tour;
        this.startsAt = startsAt;
        this.capacity = capacity;
    }

    public int remaining() {
        return capacity - reserved;
    }

    public Long getId() { return id; }
    public Tour getTour() { return tour; }
    public Instant getStartsAt() { return startsAt; }
    public int getCapacity() { return capacity; }
    public int getReserved() { return reserved; }

    public void setCapacity(int capacity) { this.capacity = capacity; }
    public void setReserved(int reserved) { this.reserved = reserved; }
}
