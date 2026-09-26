package com.gyg.prep.algorithms;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class E3_TimeSlotMergerTest {

    private final E3_TimeSlotMerger sut = new E3_TimeSlotMerger();

    @Test
    void mergesOverlappingUnsortedSlots() {
        List<TimeSlot> input = List.of(new TimeSlot(600, 700), new TimeSlot(540, 620), new TimeSlot(800, 900));

        assertThat(sut.merge(input)).containsExactly(new TimeSlot(540, 700), new TimeSlot(800, 900));
    }

    @Test
    void touchingSlotsMerge() {
        assertThat(sut.merge(List.of(new TimeSlot(60, 120), new TimeSlot(120, 180))))
                .containsExactly(new TimeSlot(60, 180));
    }

    @Test
    void containedSlotDisappears() {
        assertThat(sut.merge(List.of(new TimeSlot(0, 1000), new TimeSlot(100, 200))))
                .containsExactly(new TimeSlot(0, 1000));
    }

    @Test
    void emptyAndSingle() {
        assertThat(sut.merge(List.of())).isEmpty();
        assertThat(sut.merge(List.of(new TimeSlot(1, 2)))).containsExactly(new TimeSlot(1, 2));
    }

    @Test
    void doesNotMutateInput() {
        List<TimeSlot> input = new ArrayList<>(List.of(new TimeSlot(5, 6), new TimeSlot(1, 2)));
        sut.merge(input);
        assertThat(input).containsExactly(new TimeSlot(5, 6), new TimeSlot(1, 2));
    }
}
