package com.gyg.prep.algorithms;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class E7_EventRegistrationTest {

    private E7_EventRegistration sut;

    @BeforeEach
    void setUp() {
        sut = new E7_EventRegistration();
        sut.addEvent("e1", "Wine tasting", 2);
        sut.addEvent("e2", "Boat tour", 10);
        sut.addEvent("e3", "Art walk", 1);
    }

    @Test
    void rejectsInvalidOrDuplicateEvents() {
        assertThatThrownBy(() -> sut.addEvent("e1", "Again", 5)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> sut.addEvent("e9", "Zero", 0)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> sut.addEvent(" ", "Blank id", 3)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> sut.addEvent("e8", "", 3)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void registersUntilFull() {
        assertThat(sut.registerParticipant("e1", "ana")).isTrue();
        assertThat(sut.registerParticipant("e1", "ben")).isTrue();
        assertThat(sut.registerParticipant("e1", "cai")).as("full").isFalse();
    }

    @Test
    void registeringTwiceReturnsFalseAndDoesNotTakeASecondSpot() {
        assertThat(sut.registerParticipant("e1", "ana")).isTrue();
        assertThat(sut.registerParticipant("e1", "ana")).isFalse();
        assertThat(sut.registerParticipant("e1", "ben")).as("one spot is still free").isTrue();
    }

    @Test
    void unknownEventOnRegisterIsAnError() {
        assertThatThrownBy(() -> sut.registerParticipant("nope", "ana")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void cancellingFreesTheSpot() {
        sut.registerParticipant("e3", "ana");
        assertThat(sut.registerParticipant("e3", "ben")).isFalse();

        assertThat(sut.cancelRegistration("e3", "ana")).isTrue();
        assertThat(sut.registerParticipant("e3", "ben")).isTrue();
    }

    @Test
    void cancellingIsIdempotent() {
        assertThat(sut.cancelRegistration("e1", "ghost")).isFalse();
        assertThat(sut.cancelRegistration("nope", "ghost")).isFalse();
        sut.registerParticipant("e1", "ana");
        assertThat(sut.cancelRegistration("e1", "ana")).isTrue();
        assertThat(sut.cancelRegistration("e1", "ana")).isFalse();
    }

    @Test
    void findsEventsSortedByNameThenId() {
        sut.addEvent("e0", "Boat tour", 5);                 // same name as e2, so the id breaks the tie
        sut.registerParticipant("e1", "ana");               // Wine tasting
        sut.registerParticipant("e2", "ana");               // Boat tour
        sut.registerParticipant("e3", "ana");               // Art walk
        sut.registerParticipant("e0", "ana");               // Boat tour

        assertThat(sut.findRegisteredEvents("ana")).containsExactly("e3", "e0", "e2", "e1");
    }

    @Test
    void findReflectsCancellationsAndUnknownParticipants() {
        sut.registerParticipant("e1", "ana");
        sut.registerParticipant("e2", "ana");
        sut.cancelRegistration("e1", "ana");

        assertThat(sut.findRegisteredEvents("ana")).containsExactly("e2");
        assertThat(sut.findRegisteredEvents("nobody")).isEmpty();
    }
}
