package com.gyg.prep.algorithms;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class E2_PriceRangeSearchTest {

    private final E2_PriceRangeSearch sut = new E2_PriceRangeSearch();
    private static final int[] PRICES = {500, 1000, 1000, 1000, 2500, 4000, 9900};

    @ParameterizedTest(name = "[{0}, {1}] -> {2}")
    @CsvSource({
            "0,       100000, 7",   // everything
            "1000,    1000,   3",   // duplicates, exact bounds
            "999,     1001,   3",
            "1001,    2499,   0",   // gap between values
            "0,       499,    0",   // below everything
            "9901,    20000,  0",   // above everything
            "500,     500,    1",   // first element
            "9900,    9900,   1",   // last element
            "5000,    1000,   0",   // min > max
    })
    void countsInclusiveRange(int min, int max, int expected) {
        assertThat(sut.countInRange(PRICES, min, max)).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({"0, 10", "5, 5"})
    void emptyArray(int min, int max) {
        assertThat(sut.countInRange(new int[0], min, max)).isZero();
    }
}
