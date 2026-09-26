package com.gyg.prep.algorithms;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Reference solution: sort by start, then sweep. O(n log n). */
public final class E3_TimeSlotMerger {

    public List<TimeSlot> merge(List<TimeSlot> slots) {
        if (slots == null || slots.isEmpty()) {
            return List.of();
        }
        List<TimeSlot> sorted = new ArrayList<>(slots); // copy: never mutate the caller's list
        sorted.sort(Comparator.comparingInt(TimeSlot::start));

        List<TimeSlot> merged = new ArrayList<>();
        int curStart = sorted.get(0).start();
        int curEnd = sorted.get(0).end();
        for (TimeSlot s : sorted.subList(1, sorted.size())) {
            if (s.start() <= curEnd) {                 // "<=": touching slots merge
                curEnd = Math.max(curEnd, s.end());    // max: handles fully-contained slots
            } else {
                merged.add(new TimeSlot(curStart, curEnd));
                curStart = s.start();
                curEnd = s.end();
            }
        }
        merged.add(new TimeSlot(curStart, curEnd));
        return merged;
    }
}
