package com.gyg.prep.experiences.booking;

import com.gyg.prep.experiences.activity.Activity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
public class Booking {

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Activity activity;
    private String customerEmail;
    private int participants;
    private BigDecimal totalPrice;
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    private Instant createdAt;

    protected Booking() {
    }

    public Booking(Activity activity, String customerEmail, int participants, BigDecimal totalPrice,
                   Instant createdAt) {
        this.activity = activity;
        this.customerEmail = customerEmail;
        this.participants = participants;
        this.totalPrice = totalPrice;
        this.status = BookingStatus.CONFIRMED;
        this.createdAt = createdAt;
    }

    public void cancel() {
        this.status = BookingStatus.CANCELLED;
    }

    public Long getId() { return id; }
    public Activity getActivity() { return activity; }
    public String getCustomerEmail() { return customerEmail; }
    public int getParticipants() { return participants; }
    public BigDecimal getTotalPrice() { return totalPrice; }
    public BookingStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
}
