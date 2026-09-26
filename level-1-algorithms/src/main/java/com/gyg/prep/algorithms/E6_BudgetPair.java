package com.gyg.prep.algorithms;

import java.util.List;
import java.util.Optional;

/**
 * Exercise 6 — HashMap (Easy) — the classic "two sum", dressed as a product feature.
 *
 * A traveller has a gift card worth exactly {@code budgetCents}. Find TWO DIFFERENT activities whose prices add
 * up to exactly the budget. Return their ids as {@code List.of(firstId, secondId)}, in input order (the earlier
 * activity first). If several pairs exist, return the pair whose second element appears earliest.
 * If none exists, return Optional.empty().
 *
 * Prices are in cents to avoid floating-point surprises (ask yourself why that matters for money).
 * Target: O(n) time.
 */
public final class E6_BudgetPair {

    public record PricedActivity(long id, long priceCents) {}

    public Optional<List<Long>> findPair(List<PricedActivity> activities, long budgetCents) {
        throw new UnsupportedOperationException("TODO: implement E6");
    }
}
