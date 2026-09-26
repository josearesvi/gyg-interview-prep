package com.tourco.inventory.controller;

import com.tourco.inventory.service.NotFoundException;
import com.tourco.inventory.service.ReservationService;
import com.tourco.inventory.service.UnauthorizedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    public record ReserveRequest(int seats) {}

    public record CapacityRequest(int capacity) {}

    @PostMapping("/departures/{id}/reservations")
    public ResponseEntity<?> reserve(@PathVariable Long id, @RequestBody ReserveRequest request) {
        try {
            int left = reservationService.reserve(id, request.seats());
            if (left == -1) {
                return ResponseEntity.status(409).body(Map.of("error", "not enough seats"));
            }
            return ResponseEntity.status(201).body(Map.of("departureId", id, "seats", request.seats(), "remaining", left));
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/suppliers/departures/{id}/capacity")
    public ResponseEntity<?> updateCapacity(@RequestHeader(name = "X-Api-Key", required = false) String apiKey,
                                            @PathVariable Long id, @RequestBody CapacityRequest request) {
        try {
            return ResponseEntity.ok(DepartureView.of(reservationService.updateCapacity(apiKey, id, request.capacity())));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(401).build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
