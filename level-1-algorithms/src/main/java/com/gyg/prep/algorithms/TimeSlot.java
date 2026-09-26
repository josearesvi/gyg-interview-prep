package com.gyg.prep.algorithms;

/** A half-open availability slot [start, end) in minutes since midnight. */
public record TimeSlot(int start, int end) {
    public TimeSlot {
        if (end < start) {
            throw new IllegalArgumentException("end < start: " + start + ".." + end);
        }
    }
}
