package com.gyg.prep.bookings.booking;

import com.gyg.prep.bookings.activity.Activity;
import com.gyg.prep.bookings.activity.ActivityRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class BookingControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ActivityRepository activities;

    private Long activityId;

    @BeforeEach
    void setUp() {
        activityId = activities.save(new Activity("Sagrada Familia", "Barcelona", new BigDecimal("39.50"), "EUR",
                4, LocalDate.of(2026, 1, 1), LocalDate.of(2026, 12, 31))).getId();
    }

    private ResultActions book(int participants, String currency) throws Exception {
        // Built at runtime on purpose: the JSON parser never hands back the interned "EUR" literal.
        String body = """
                {"activityId": %d, "customerEmail": "ana@example.com", "participants": %d, "currency": "%s"}
                """.formatted(activityId, participants, currency);
        return mvc.perform(post("/bookings").contentType(MediaType.APPLICATION_JSON).content(body));
    }

    @Test
    void booksInTheActivityCurrency() throws Exception {
        book(2, "EUR")
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.totalPrice").value(79.00))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    void rejectsAnotherCurrency() throws Exception {
        book(1, "USD").andExpect(status().isConflict());
    }

    @Test
    void canBookTheLastRemainingSpots() throws Exception {
        book(2, "EUR").andExpect(status().isCreated());
        book(2, "EUR").andExpect(status().isCreated());   // capacity is 4: exactly full is fine
        book(1, "EUR").andExpect(status().isConflict());  // now it is over capacity
    }

    @Test
    void cancellingReleasesTheSpots() throws Exception {
        String created = book(4, "EUR").andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        Integer bookingId = JsonPath.read(created, "$.id");

        mvc.perform(post("/bookings/{id}/cancel", bookingId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));

        book(4, "EUR").andExpect(status().isCreated());
    }

    @Test
    void validatesTheRequest() throws Exception {
        book(0, "EUR").andExpect(status().isBadRequest());
    }

    @Test
    void unknownActivityIs404() throws Exception {
        activityId = 999_999L;
        book(1, "EUR").andExpect(status().isNotFound());
    }
}
