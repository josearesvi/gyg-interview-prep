package com.gyg.prep.bookings.activity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Activity {

    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String city;
    private BigDecimal pricePerPerson;
    private String currency;
    private int capacity;
    private LocalDate availableFrom;
    private LocalDate availableTo;

    protected Activity() {
    }

    public Activity(String title, String city, BigDecimal pricePerPerson, String currency, int capacity,
                    LocalDate availableFrom, LocalDate availableTo) {
        this.title = title;
        this.city = city;
        this.pricePerPerson = pricePerPerson;
        this.currency = currency;
        this.capacity = capacity;
        this.availableFrom = availableFrom;
        this.availableTo = availableTo;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getCity() { return city; }
    public BigDecimal getPricePerPerson() { return pricePerPerson; }
    public String getCurrency() { return currency; }
    public int getCapacity() { return capacity; }
    public LocalDate getAvailableFrom() { return availableFrom; }
    public LocalDate getAvailableTo() { return availableTo; }
}
