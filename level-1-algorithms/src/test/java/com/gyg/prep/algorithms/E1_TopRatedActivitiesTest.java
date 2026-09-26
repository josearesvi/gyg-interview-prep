package com.gyg.prep.algorithms;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class E1_TopRatedActivitiesTest {

    private final E1_TopRatedActivities sut = new E1_TopRatedActivities();

    private static Activity a(long id, double rating, int reviews) {
        return new Activity(id, "a" + id, "Rome", rating, reviews, BigDecimal.TEN);
    }

    @Test
    void ordersByRatingThenReviewsThenId() {
        List<Activity> input = List.of(a(1, 4.5, 10), a(2, 4.9, 3), a(3, 4.5, 200), a(4, 4.5, 200), a(5, 3.0, 999));

        assertThat(sut.topK(input, 4)).extracting(Activity::id).containsExactly(2L, 3L, 4L, 1L);
    }

    @Test
    void kLargerThanInputReturnsEverythingSorted() {
        assertThat(sut.topK(List.of(a(1, 1.0, 1), a(2, 2.0, 1)), 10)).extracting(Activity::id).containsExactly(2L, 1L);
    }

    @Test
    void zeroOrNegativeKAndEmptyInput() {
        assertThat(sut.topK(List.of(a(1, 1.0, 1)), 0)).isEmpty();
        assertThat(sut.topK(List.of(a(1, 1.0, 1)), -3)).isEmpty();
        assertThat(sut.topK(List.of(), 3)).isEmpty();
    }

    @Test
    void doesNotMutateInput() {
        List<Activity> input = new ArrayList<>(List.of(a(1, 1.0, 1), a(2, 5.0, 1)));
        sut.topK(input, 1);
        assertThat(input).extracting(Activity::id).containsExactly(1L, 2L);
    }
}
