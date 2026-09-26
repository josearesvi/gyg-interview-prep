package com.tourco.inventory.service;

import com.tourco.inventory.config.InventoryProperties;
import com.tourco.inventory.model.Departure;
import com.tourco.inventory.repository.DepartureRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Service
public class ReservationService {

    private final DepartureRepository departureRepository;
    private final InventoryProperties properties;

    public ReservationService(DepartureRepository departureRepository, InventoryProperties properties) {
        this.departureRepository = departureRepository;
        this.properties = properties;
    }

    /**
     * REQUEST 3: was read-modify-write (findById, check, setReserved, save). Under concurrency, two requests could
     * both read reserved=18 and both write 20, or one could read 18 and the other write 20 → 23 people.
     * Now: a single conditional UPDATE. Alternatives: @Version optimistic locking + retry, or SELECT ... FOR UPDATE.
     * Returns the seats left, or -1 if there was not enough room (kept for the controller's contract).
     */
    @Transactional
    public int reserve(Long departureId, int seats) {
        if (seats < 1 || seats > properties.maxSeatsPerReservation()) {
            throw new IllegalArgumentException("seats must be between 1 and " + properties.maxSeatsPerReservation());
        }
        int updated = departureRepository.tryReserve(departureId, seats);
        Departure d = departureRepository.findById(departureId)
                .orElseThrow(() -> new NotFoundException("departure " + departureId + " not found"));
        return updated == 1 ? d.remaining() : -1;
    }

    @Transactional
    public Departure updateCapacity(String apiKey, Long departureId, int capacity) {
        if (apiKey == null || !constantTimeEquals(apiKey, properties.supplierApiKey())) {
            throw new UnauthorizedException();
        }
        Departure d = departureRepository.findById(departureId)
                .orElseThrow(() -> new NotFoundException("departure " + departureId + " not found"));
        if (capacity < d.getReserved()) {
            throw new IllegalArgumentException("capacity cannot be below the " + d.getReserved() + " seats already reserved");
        }
        d.setCapacity(capacity);
        return d;
    }

    /** String.equals leaks, through timing, how many leading characters matched. */
    private static boolean constantTimeEquals(String a, String b) {
        return MessageDigest.isEqual(a.getBytes(StandardCharsets.UTF_8), b.getBytes(StandardCharsets.UTF_8));
    }
}
