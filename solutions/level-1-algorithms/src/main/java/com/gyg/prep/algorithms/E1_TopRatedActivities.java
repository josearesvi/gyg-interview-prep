package com.gyg.prep.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Reference solution. O(n log k) time, O(k) extra space, using a MIN-heap of size k: the root is the worst of
 * the current best k, so every new candidate only has to beat the root.
 * Sort-then-slice is O(n log n). It is simpler and fine for small n, so say that out loud in the interview.
 */
public final class E1_TopRatedActivities {

    static final Comparator<Activity> BEST_FIRST = Comparator
            .comparingDouble(Activity::rating).reversed()
            .thenComparing(Comparator.comparingInt(Activity::reviewCount).reversed())
            .thenComparingLong(Activity::id);

    public List<Activity> topK(List<Activity> activities, int k) {
        if (k <= 0 || activities == null || activities.isEmpty()) {
            return List.of();
        }
        PriorityQueue<Activity> heap = new PriorityQueue<>(BEST_FIRST.reversed()); // worst on top
        for (Activity a : activities) {
            heap.offer(a);
            if (heap.size() > k) {
                heap.poll();
            }
        }
        List<Activity> result = new ArrayList<>(heap);
        result.sort(BEST_FIRST);
        return Collections.unmodifiableList(result);
    }
}
