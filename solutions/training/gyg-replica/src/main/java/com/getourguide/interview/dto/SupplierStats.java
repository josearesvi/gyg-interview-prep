package com.getourguide.interview.dto;

/**
 * Moved here from the repository package (it's an API shape, not persistence).
 * Filled by a JPQL constructor expression: the argument ORDER and TYPES must match the query exactly
 * (count → Long, sum(int) → Long, avg → Double). Swap two Long arguments and you get silently wrong numbers.
 */
public record SupplierStats(String supplierName, Long activityCount, Long totalRevenue, Double averageRating) {

    public SupplierStats withRoundedRating() {
        return new SupplierStats(supplierName, activityCount, totalRevenue,
                averageRating == null ? null : Math.round(averageRating * 100) / 100.0);
    }
}
