package com.gyg.prep.algorithms;

import java.util.List;

/**
 * Exercise 1 — Sorting / heaps (Easy).
 *
 * The search page shows the "top k" activities of a city. Return the k best activities ordered by:
 *   1. rating, highest first
 *   2. reviewCount, highest first (tie-break)
 *   3. id, lowest first (a stable, deterministic final tie-break)
 *
 * Rules:
 *   - k <= 0 or an empty input returns an empty list.
 *   - k larger than the input returns all of them, ordered.
 *   - Do not mutate the input list.
 *
 * Talk-aloud prompts: what is the complexity of sort-then-slice? Can you do O(n log k)? When is it worth it?
 */
public final class E1_TopRatedActivities {

    public List<Activity> topK(List<Activity> activities, int k) {
        throw new UnsupportedOperationException("TODO: implement E1");
    }
}
