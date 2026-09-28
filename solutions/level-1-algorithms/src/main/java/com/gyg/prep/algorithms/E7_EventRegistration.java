package com.gyg.prep.algorithms;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Reference solution. Two maps, one per direction: event → participants (for capacity checks) and
 * participant → events (so findRegisteredEvents never scans every event). Both are updated together.
 *
 * Talking points: why two indexes (the read pattern), what "idempotent cancel" buys a client that retries, and for
 * the thread-safety follow-up, the synchronized methods / a lock per event / ConcurrentHashMap.compute.
 */
public final class E7_EventRegistration {

    private record Event(String id, String name, int capacity, Set<String> participants) {}

    private final Map<String, Event> events = new HashMap<>();
    private final Map<String, Set<String>> eventsByParticipant = new HashMap<>();

    public void addEvent(String eventId, String name, int capacity) {
        if (eventId == null || eventId.isBlank() || name == null || name.isBlank() || capacity < 1) {
            throw new IllegalArgumentException("invalid event");
        }
        if (events.putIfAbsent(eventId, new Event(eventId, name, capacity, new HashSet<>())) != null) {
            throw new IllegalArgumentException("duplicate event " + eventId);
        }
    }

    public boolean registerParticipant(String eventId, String participantId) {
        Event event = events.get(eventId);
        if (event == null) {
            throw new IllegalArgumentException("unknown event " + eventId);
        }
        if (event.participants().contains(participantId) || event.participants().size() >= event.capacity()) {
            return false;
        }
        event.participants().add(participantId);
        eventsByParticipant.computeIfAbsent(participantId, p -> new HashSet<>()).add(eventId);
        return true;
    }

    public boolean cancelRegistration(String eventId, String participantId) {
        Event event = events.get(eventId);
        if (event == null || !event.participants().remove(participantId)) {
            return false;
        }
        Set<String> mine = eventsByParticipant.get(participantId);
        mine.remove(eventId);
        if (mine.isEmpty()) {
            eventsByParticipant.remove(participantId);
        }
        return true;
    }

    public List<String> findRegisteredEvents(String participantId) {
        return eventsByParticipant.getOrDefault(participantId, Set.of()).stream()
                .map(events::get)
                .sorted(Comparator.comparing(Event::name).thenComparing(Event::id))
                .map(Event::id)
                .toList();
    }
}
