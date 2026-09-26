package com.gyg.prep.experiences.booking;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.Instant;

/**
 * FEATURE 2. The key is the PRIMARY KEY, so if two retries race with the same key, the database rejects the
 * second insert (unique violation) and at most one booking commits.
 * In production you would also expire records (e.g. after 24h) and scope keys per customer.
 */
@Entity
public class IdempotencyRecord {

    @Id
    private String idempotencyKey;
    private String requestFingerprint;
    private Long bookingId;
    private Instant createdAt;

    protected IdempotencyRecord() {
    }

    public IdempotencyRecord(String idempotencyKey, String requestFingerprint, Long bookingId, Instant createdAt) {
        this.idempotencyKey = idempotencyKey;
        this.requestFingerprint = requestFingerprint;
        this.bookingId = bookingId;
        this.createdAt = createdAt;
    }

    public String getRequestFingerprint() { return requestFingerprint; }
    public Long getBookingId() { return bookingId; }
}
