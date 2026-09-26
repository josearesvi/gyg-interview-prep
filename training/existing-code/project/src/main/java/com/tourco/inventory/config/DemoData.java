package com.tourco.inventory.config;

import com.tourco.inventory.model.Departure;
import com.tourco.inventory.model.Tour;
import com.tourco.inventory.repository.DepartureRepository;
import com.tourco.inventory.repository.TourRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
@Profile("!test")
public class DemoData implements CommandLineRunner {

    private final TourRepository tours;
    private final DepartureRepository departures;

    public DemoData(TourRepository tours, DepartureRepository departures) {
        this.tours = tours;
        this.departures = departures;
    }

    @Override
    public void run(String... args) {
        Tour colosseum = tours.save(new Tour("Colosseum underground tour", "Rome", "Europe/Rome", "sup-roma"));
        Tour night = tours.save(new Tour("Rome by night: ghosts & legends", "Rome", "Europe/Rome", "sup-roma"));
        Tour wall = tours.save(new Tour("Berlin Wall bike tour", "Berlin", "Europe/Berlin", "sup-berlin"));

        departures.save(new Departure(colosseum, at("2026-10-01T10:00", "Europe/Rome"), 20));
        departures.save(new Departure(colosseum, at("2026-10-01T14:00", "Europe/Rome"), 20));
        departures.save(new Departure(night, at("2026-10-02T00:30", "Europe/Rome"), 12));
        departures.save(new Departure(wall, at("2026-10-01T11:00", "Europe/Berlin"), 15));
    }

    private static java.time.Instant at(String localDateTime, String zone) {
        return LocalDateTime.parse(localDateTime).atZone(ZoneId.of(zone)).toInstant();
    }
}
