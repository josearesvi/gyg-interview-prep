package com.tourco.inventory;

import com.tourco.inventory.model.Departure;
import com.tourco.inventory.model.Tour;
import com.tourco.inventory.repository.DepartureRepository;
import com.tourco.inventory.repository.TourRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static com.tourco.inventory.Fixtures.local;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class TourApiTest {

    @Autowired MockMvc mvc;
    @Autowired TourRepository tours;
    @Autowired DepartureRepository departures;

    Tour colosseum;
    Tour ghosts;

    @BeforeEach
    void setUp() {
        colosseum = tours.save(new Tour("Colosseum underground", "Rome", "Europe/Rome", "sup-roma"));
        ghosts = tours.save(new Tour("Rome by night", "Rome", "Europe/Rome", "sup-roma"));
        tours.save(new Tour("Wall bike tour", "Berlin", "Europe/Berlin", "sup-berlin"));

        departures.save(new Departure(colosseum, local("2026-10-01T10:00", "Europe/Rome"), 20));
        Departure full = new Departure(colosseum, local("2026-10-01T14:00", "Europe/Rome"), 20);
        full.setReserved(20);
        departures.save(full);
        departures.save(new Departure(ghosts, local("2026-10-02T00:30", "Europe/Rome"), 12));
    }

    @Test
    void listsToursOfACityWithOpenDepartures() throws Exception {
        mvc.perform(get("/tours").param("city", "rome"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[?(@.name == 'Colosseum underground')].openDepartures").value(1));
    }

    @Test
    void availabilityListsTheDaysDepartures() throws Exception {
        mvc.perform(get("/tours/{id}/availability", colosseum.getId()).param("date", "2026-10-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].remaining").value(20))
                .andExpect(jsonPath("$[1].remaining").value(0));
    }

    @Test
    void nightDepartureIsListedOnItsLocalDate() throws Exception {
        // 00:30 on 2 Oct in Rome is 22:30 on 1 Oct in UTC. Travellers and suppliers think in local time.
        mvc.perform(get("/tours/{id}/availability", ghosts.getId()).param("date", "2026-10-02"))
                .andExpect(jsonPath("$", hasSize(1)));
        mvc.perform(get("/tours/{id}/availability", ghosts.getId()).param("date", "2026-10-01"))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void unknownTourIs404() throws Exception {
        mvc.perform(get("/tours/{id}/availability", 999_999).param("date", "2026-10-01"))
                .andExpect(status().isNotFound());
    }
}
