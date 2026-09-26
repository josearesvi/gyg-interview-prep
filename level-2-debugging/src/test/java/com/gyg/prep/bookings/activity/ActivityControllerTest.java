package com.gyg.prep.bookings.activity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ActivityControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ActivityRepository activities;

    private Activity colosseum;

    @BeforeEach
    void setUp() {
        colosseum = activities.save(new Activity("Colosseum", "Rome", new BigDecimal("49.90"), "EUR", 10,
                LocalDate.of(2026, 4, 1), LocalDate.of(2026, 10, 31)));
        activities.save(new Activity("Wall tour", "Berlin", new BigDecimal("29.00"), "EUR", 10,
                LocalDate.of(2026, 3, 1), LocalDate.of(2026, 11, 30)));
    }

    @Test
    void getsAnExistingActivity() throws Exception {
        mvc.perform(get("/activities/{id}", colosseum.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Colosseum"));
    }

    @Test
    void unknownActivityIs404() throws Exception {
        mvc.perform(get("/activities/{id}", 999_999)).andExpect(status().isNotFound());
    }

    @Test
    void cityFilterIsCaseInsensitive() throws Exception {
        mvc.perform(get("/activities").param("city", "rome"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Colosseum"));
    }

    @Test
    void seasonBoundariesAreInclusive() throws Exception {
        mvc.perform(get("/activities").param("city", "Rome").param("date", "2026-04-01"))
                .andExpect(jsonPath("$", hasSize(1)));
        mvc.perform(get("/activities").param("city", "Rome").param("date", "2026-10-31"))
                .andExpect(jsonPath("$", hasSize(1)));
        mvc.perform(get("/activities").param("city", "Rome").param("date", "2026-11-01"))
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
