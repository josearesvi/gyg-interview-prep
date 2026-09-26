package com.tourco.inventory;

import com.tourco.inventory.model.Departure;
import com.tourco.inventory.model.Tour;
import com.tourco.inventory.repository.DepartureRepository;
import com.tourco.inventory.repository.TourRepository;
import com.tourco.inventory.service.ReservationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.tourco.inventory.Fixtures.local;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Tests for requests 2 and 3. Deliberately NOT @Transactional, so the concurrency test uses real transactions. */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SolutionTest {

    @Autowired MockMvc mvc;
    @Autowired TourRepository tours;
    @Autowired DepartureRepository departures;
    @Autowired ReservationService reservations;

    @AfterEach
    void cleanUp() {
        departures.deleteAll();
        tours.deleteAll();
    }

    @Test
    void minSeatsFiltersAndIsValidated() throws Exception {
        Tour tour = tours.save(new Tour("Colosseum", "Rome", "Europe/Rome", "s"));
        Departure four = new Departure(tour, local("2026-10-01T10:00", "Europe/Rome"), 10);
        four.setReserved(6);
        departures.save(four);
        Departure three = new Departure(tour, local("2026-10-01T12:00", "Europe/Rome"), 10);
        three.setReserved(7);
        departures.save(three);

        mvc.perform(get("/tours/{id}/availability", tour.getId()).param("date", "2026-10-01"))
                .andExpect(jsonPath("$", hasSize(2)));
        mvc.perform(get("/tours/{id}/availability", tour.getId()).param("date", "2026-10-01").param("minSeats", "4"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].remaining").value(4));
        mvc.perform(get("/tours/{id}/availability", tour.getId()).param("date", "2026-10-01").param("minSeats", "0"))
                .andExpect(status().isBadRequest());
        mvc.perform(get("/tours/{id}/availability", tour.getId()).param("date", "2026-10-01").param("minSeats", "21"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void concurrentReservationsNeverOverbook() throws Exception {
        Tour tour = tours.save(new Tour("Colosseum", "Rome", "Europe/Rome", "s"));
        Long id = departures.save(new Departure(tour, local("2026-10-01T10:00", "Europe/Rome"), 20)).getId();

        ExecutorService pool = Executors.newFixedThreadPool(30);
        CountDownLatch go = new CountDownLatch(1);
        List<Future<Integer>> results = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            results.add(pool.submit(() -> {
                go.await();
                return reservations.reserve(id, 1);
            }));
        }
        go.countDown();
        int ok = 0;
        for (Future<Integer> r : results) {
            if (r.get() >= 0) ok++;
        }
        pool.shutdown();

        assertThat(ok).isEqualTo(20);
        assertThat(departures.findById(id).orElseThrow().getReserved()).isEqualTo(20);
    }
}
