package com.gyg.prep.algorithms;

import com.gyg.prep.algorithms.E6_BudgetPair.PricedActivity;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class E6_BudgetPairTest {

    private final E6_BudgetPair sut = new E6_BudgetPair();

    @Test
    void findsPairInInputOrder() {
        List<PricedActivity> xs = List.of(
                new PricedActivity(10, 2000), new PricedActivity(11, 4500), new PricedActivity(12, 3000));

        assertThat(sut.findPair(xs, 5000)).contains(List.of(10L, 12L));
    }

    @Test
    void sameActivityCannotBeUsedTwice() {
        assertThat(sut.findPair(List.of(new PricedActivity(1, 2500)), 5000)).isEmpty();
    }

    @Test
    void twoActivitiesWithTheSamePriceAreFine() {
        List<PricedActivity> xs = List.of(new PricedActivity(1, 2500), new PricedActivity(2, 2500));
        assertThat(sut.findPair(xs, 5000)).contains(List.of(1L, 2L));
    }

    @Test
    void returnsPairCompletedEarliest() {
        List<PricedActivity> xs = List.of(
                new PricedActivity(1, 1000), new PricedActivity(2, 3000),
                new PricedActivity(3, 2000), new PricedActivity(4, 4000));
        // (2,3) completes at index 2; (1,4) completes at index 3.
        assertThat(sut.findPair(xs, 5000)).contains(List.of(2L, 3L));
    }

    @Test
    void noPair() {
        assertThat(sut.findPair(List.of(), 100)).isEmpty();
        assertThat(sut.findPair(List.of(new PricedActivity(1, 10), new PricedActivity(2, 20)), 100)).isEmpty();
    }
}
