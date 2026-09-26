package com.tourco.inventory.service;

import com.tourco.inventory.model.Departure;
import com.tourco.inventory.model.Tour;
import com.tourco.inventory.repository.DepartureRepository;
import com.tourco.inventory.repository.TourRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
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

    @Transactional(readOnly = true)
    public List<Map<String, Object>> listTours(String city) {
        List<Tour> tours = city == null ? tourRepository.findAll() : tourRepository.findByCityIgnoreCase(city);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Tour t : tours) {
            Map<String, Object> m = new HashMap<>();
            m.put("id", t.getId());
            m.put("name", t.getName());
            m.put("city", t.getCity());
            // how many departures are still bookable
            List<Departure> deps = departureRepository.findByTourIdOrderByStartsAt(t.getId());
            int open = 0;
            for (Departure d : deps) {
                if (d.remaining() > 0) open++;
            }
            m.put("openDepartures", open);
            result.add(m);
        }
        return result;
    }

    /** Departures of a tour on a given calendar day, with remaining seats. */
    @Transactional(readOnly = true)
    public List<Departure> availability(Long tourId, LocalDate date) {
        tourRepository.findById(tourId).orElseThrow(() -> new NotFoundException("tour " + tourId + " not found"));
        List<Departure> result = new ArrayList<>();
        for (Departure d : departureRepository.findByTourIdOrderByStartsAt(tourId)) {
            LocalDate day = d.getStartsAt().atZone(ZoneOffset.UTC).toLocalDate();
            if (day.equals(date)) {
                result.add(d);
            }
        }
        return result;
    }
}
