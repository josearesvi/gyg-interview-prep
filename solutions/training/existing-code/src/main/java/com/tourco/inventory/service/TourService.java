package com.tourco.inventory.service;

import com.tourco.inventory.model.Departure;
import com.tourco.inventory.model.Tour;
import com.tourco.inventory.repository.DepartureRepository;
import com.tourco.inventory.repository.TourRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TourService {

    private final TourRepository tourRepository;
    private final DepartureRepository departureRepository;

    public TourService(TourRepository tourRepository, DepartureRepository departureRepository) {
        this.tourRepository = tourRepository;
        this.departureRepository = departureRepository;
    }

    public record TourSummary(Long id, String name, String city, long openDepartures) {}

    /** REQUEST 3: 2 queries total (tours + one grouped count), instead of 1 + one per tour. */
    @Transactional(readOnly = true)
    public List<TourSummary> listTours(String city) {
        List<Tour> tours = city == null ? tourRepository.findAll() : tourRepository.findByCityIgnoreCase(city);
        if (tours.isEmpty()) {
            return List.of();
        }
        Map<Long, Long> open = new HashMap<>();
        for (Object[] row : departureRepository.countOpenDepartures(tours.stream().map(Tour::getId).toList())) {
            open.put((Long) row[0], (Long) row[1]);
        }
        return tours.stream()
                .map(t -> new TourSummary(t.getId(), t.getName(), t.getCity(), open.getOrDefault(t.getId(), 0L)))
                .toList();
    }

    /**
     * REQUEST 1: a "day" is the tour's LOCAL calendar day. The bug converted startsAt to a UTC date, so a 00:30 Rome
     * departure (22:30 UTC the day before) landed on the previous day.
     * REQUEST 2: minSeats filter (1 = no filtering, since remaining >= 0 always holds... but full departures must stay
     * visible without the filter, so callers pass 0 for "no filter").
     */
    @Transactional(readOnly = true)
    public List<Departure> availability(Long tourId, LocalDate date, int minSeats) {
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new NotFoundException("tour " + tourId + " not found"));
        ZoneId zone = ZoneId.of(tour.getTimeZone());
        return departureRepository.findAvailable(tourId,
                date.atStartOfDay(zone).toInstant(),
                date.plusDays(1).atStartOfDay(zone).toInstant(),   // half-open [from, to): DST-safe (23h/25h days)
                minSeats);
    }
}
