package com.gyg.prep.algorithms;

/** Reference solution: two binary searches, O(log n). */
public final class E2_PriceRangeSearch {

    public int countInRange(int[] sortedPrices, int min, int max) {
        if (sortedPrices == null || min > max) {
            return 0;
        }
        return upperBound(sortedPrices, max) - lowerBound(sortedPrices, min);
    }

    /** Index of the first element >= target (or length). */
    static int lowerBound(int[] a, int target) {
        int lo = 0, hi = a.length;              // search in [lo, hi)
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;       // no int overflow
            if (a[mid] < target) lo = mid + 1; else hi = mid;
        }
        return lo;
    }

    /** Index of the first element > target (or length). */
    static int upperBound(int[] a, int target) {
        int lo = 0, hi = a.length;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (a[mid] <= target) lo = mid + 1; else hi = mid;
        }
        return lo;
    }
}
