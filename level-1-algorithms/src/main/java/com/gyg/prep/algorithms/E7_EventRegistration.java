package com.gyg.prep.algorithms;

import java.util.List;

/**
 * Exercise 7: HashMaps + sets + sorting, in the style of GetYourGuide's HackerRank take-home (Medium).
 * Candidates reported (Glassdoor, April 2026) an "Event Registration System" with these four operations.
 * The spec below is ours. Read it like a take-home: every rule is tested.
 *
 *  - addEvent(eventId, name, capacity): a blank id/name, capacity < 1 or a duplicate eventId → IllegalArgumentException.
 *  - registerParticipant(eventId, participantId): an unknown event → IllegalArgumentException.
 *    Returns true if registered; false if already registered or the event is full.
 *  - cancelRegistration(eventId, participantId): true if the participant was registered (their spot is freed),
 *    false otherwise (including an unknown event: cancelling is idempotent).
 *  - findRegisteredEvents(participantId): the ids of the events they're registered for, sorted by event
 *    name, then by id. Unknown participant → empty list.
 *
 * Targets: register/cancel O(1) on average; findRegisteredEvents O(k log k) for k events, never a scan of all events.
 * Follow-ups an interviewer may add: a waitlist that is promoted automatically on cancel; thread-safety.
 */
public final class E7_EventRegistration {

    public void addEvent(String eventId, String name, int capacity) {
        throw new UnsupportedOperationException("TODO: implement E7");
    }

    public boolean registerParticipant(String eventId, String participantId) {
        throw new UnsupportedOperationException("TODO: implement E7");
    }

    public boolean cancelRegistration(String eventId, String participantId) {
        throw new UnsupportedOperationException("TODO: implement E7");
    }

    public List<String> findRegisteredEvents(String participantId) {
        throw new UnsupportedOperationException("TODO: implement E7");
    }
}
