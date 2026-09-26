package com.tourco.inventory;

import com.tourco.inventory.model.Departure;
import com.tourco.inventory.model.Tour;
import com.tourco.inventory.repository.DepartureRepository;
import com.tourco.inventory.repository.TourRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static com.tourco.inventory.Fixtures.local;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ReservationApiTest {

    @Autowired MockMvc mvc;
    @Autowired TourRepository tours;
    @Autowired DepartureRepository departures;
    @Value("${inventory.supplier-api-key}") String apiKey;

    Long departureId;

    @BeforeEach
    void setUp() {
        Tour tour = tours.save(new Tour("Colosseum underground", "Rome", "Europe/Rome", "sup-roma"));
        departureId = departures.save(new Departure(tour, local("2026-10-01T10:00", "Europe/Rome"), 5)).getId();
    }

    ResultActions reserve(int seats) throws Exception {
        return mvc.perform(post("/departures/{id}/reservations", departureId)
                .contentType(MediaType.APPLICATION_JSON).content("{\"seats\": " + seats + "}"));
    }

    @Test
    void reservesSeats() throws Exception {
        reserve(3).andExpect(status().isCreated()).andExpect(jsonPath("$.remaining").value(2));
        reserve(2).andExpect(status().isCreated()).andExpect(jsonPath("$.remaining").value(0));
        reserve(1).andExpect(status().isConflict());
    }

    @Test
    void rejectsInvalidSeatCounts() throws Exception {
        reserve(0).andExpect(status().isBadRequest());
        reserve(11).andExpect(status().isBadRequest());
    }

    @Test
    void suppliersNeedTheirApiKeyToChangeCapacity() throws Exception {
        String body = "{\"capacity\": 8}";
        mvc.perform(put("/suppliers/departures/{id}/capacity", departureId)
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isUnauthorized());
        mvc.perform(put("/suppliers/departures/{id}/capacity", departureId).header("X-Api-Key", "wrong")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isUnauthorized());
        mvc.perform(put("/suppliers/departures/{id}/capacity", departureId).header("X-Api-Key", apiKey)
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.capacity").value(8));
    }
}
