package com.gyg.prep.algorithms;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class E5_CityTransfersTest {

    private final E5_CityTransfers sut = new E5_CityTransfers();

    // Declared in ONE direction only: your adjacency list must add the reverse edge.
    private static final Map<String, List<String>> ROUTES = Map.of(
            "Berlin", List.of("Prague", "Munich"),
            "Prague", List.of("Vienna"),
            "Munich", List.of("Zurich"),
            "Vienna", List.of("Budapest"),
            "Lisbon", List.of("Porto"));

    @Test
    void shortestNumberOfTransfers() {
        assertThat(sut.minTransfers(ROUTES, "Berlin", "Budapest")).isEqualTo(3);
        assertThat(sut.minTransfers(ROUTES, "Zurich", "Vienna")).isEqualTo(4);
    }

    @Test
    void routesAreBidirectional() {
        assertThat(sut.minTransfers(ROUTES, "Budapest", "Berlin")).isEqualTo(3);
    }

    @Test
    void sameCityIsZero() {
        assertThat(sut.minTransfers(ROUTES, "Berlin", "Berlin")).isZero();
    }

    @Test
    void unreachableOrUnknownIsMinusOne() {
        assertThat(sut.minTransfers(ROUTES, "Berlin", "Lisbon")).isEqualTo(-1);
        assertThat(sut.minTransfers(ROUTES, "Berlin", "Atlantis")).isEqualTo(-1);
    }
}
