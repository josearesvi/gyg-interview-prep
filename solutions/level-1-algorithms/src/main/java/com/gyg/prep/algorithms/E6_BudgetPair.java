package com.gyg.prep.algorithms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Reference solution: one pass, remembering the price seen so far -> id. O(n) time, O(n) space. */
public final class E6_BudgetPair {

    public record PricedActivity(long id, long priceCents) {}

    public Optional<List<Long>> findPair(List<PricedActivity> activities, long budgetCents) {
        Map<Long, Long> firstIdByPrice = new HashMap<>();
        for (PricedActivity a : activities) {
            Long partner = firstIdByPrice.get(budgetCents - a.priceCents());
            if (partner != null) {
                return Optional.of(List.of(partner, a.id()));
            }
            firstIdByPrice.putIfAbsent(a.priceCents(), a.id()); // keep the EARLIEST id for each price
        }
        return Optional.empty();
    }
}
