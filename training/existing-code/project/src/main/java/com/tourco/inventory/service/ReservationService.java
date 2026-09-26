package com.tourco.inventory.service;

import com.tourco.inventory.config.InventoryProperties;
import com.tourco.inventory.model.Departure;
import com.tourco.inventory.repository.DepartureRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservationService {

    private final DepartureRepository departureRepository;
    private final InventoryProperties properties;

    public ReservationService(DepartureRepository departureRepository, InventoryProperties properties) {
        this.departureRepository = departureRepository;
        this.properties = properties;
    }

    /**
     * Holds seats on a departure. Returns the seats left, or -1 if there was not enough room.
     */
    @Transactional
    public int reserve(Long departureId, int seats) {
        if (seats < 1 || seats > properties.maxSeatsPerReservation()) {
            throw new IllegalArgumentException("seats must be between 1 and " + properties.maxSeatsPerReservation());
        }
        Departure d = departureRepository.findById(departureId)
                .orElseThrow(() -> new NotFoundException("departure " + departureId + " not found"));
        if (d.remaining() < seats) {
            return -1;
        }
        d.setReserved(d.getReserved() + seats);
        departureRepository.save(d);
        return d.remaining();
    }

    /** Suppliers adjust capacity (e.g. a second guide becomes available). */
    @Transactional
    public Departure updateCapacity(String apiKey, Long departureId, int capacity) {
        if (apiKey == null || !apiKey.equals(properties.supplierApiKey())) {
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
}
