package com.gyg.prep.experiences.activity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
public class Activity {

    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String city;
    private BigDecimal price;
    private double rating;
    private int capacity;
    private Instant startsAt;

    protected Activity() {
    }

    public Activity(String title, String city, BigDecimal price, double rating, int capacity, Instant startsAt) {
        this.title = title;
        this.city = city;
        this.price = price;
        this.rating = rating;
        this.capacity = capacity;
        this.startsAt = startsAt;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getCity() { return city; }
    public BigDecimal getPrice() { return price; }
    public double getRating() { return rating; }
    public int getCapacity() { return capacity; }
    public Instant getStartsAt() { return startsAt; }
}
