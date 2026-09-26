package com.gyg.prep.experiences;

import com.gyg.prep.experiences.activity.ActivityRepository;
import com.gyg.prep.experiences.booking.BookingRepository;
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

import static com.gyg.prep.experiences.TestData.activity;
import static com.gyg.prep.experiences.TestData.bookingJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * FEATURE 2: idempotent booking creation.
 *
 * Mobile clients retry POST /bookings when the network drops, and customers get charged twice. Support an
 * optional "Idempotency-Key" header:
 *  - same key + same request body -> no new booking; return the ORIGINAL booking (201, same id)
 *  - same key + DIFFERENT body    -> 422 Unprocessable Entity
 *  - no header                    -> behaves exactly as today
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class Feature2IdempotencyTest {

    @Autowired MockMvc mvc;
    @Autowired ActivityRepository activities;
    @Autowired BookingRepository bookings;

    long activityId;

    @BeforeEach
    void setUp() {
        activityId = activities.save(activity("Colosseum", "Rome", "49.90", 4.7, 10)).getId();
    }

    ResultActions book(String key, int participants) throws Exception {
        return mvc.perform(post("/bookings").header("Idempotency-Key", key)
                .contentType(MediaType.APPLICATION_JSON).content(bookingJson(activityId, participants)));
    }

    @Test
    void retryReturnsTheOriginalBooking() throws Exception {
        String first = book("key-123", 2).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        String retry = book("key-123", 2).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();

        assertThat((Integer) JsonPath.read(retry, "$.id")).isEqualTo(JsonPath.read(first, "$.id"));
        assertThat(bookings.countBookedSeats(activityId)).isEqualTo(2);
    }

    @Test
    void reusingAKeyForADifferentRequestIsRejected() throws Exception {
        book("key-456", 2).andExpect(status().isCreated());
        book("key-456", 3).andExpect(status().isUnprocessableEntity());
        assertThat(bookings.countBookedSeats(activityId)).isEqualTo(2);
    }

    @Test
    void differentKeysCreateDifferentBookings() throws Exception {
        book("key-a", 1).andExpect(status().isCreated());
        book("key-b", 1).andExpect(status().isCreated());
        assertThat(bookings.countBookedSeats(activityId)).isEqualTo(2);
    }
}
