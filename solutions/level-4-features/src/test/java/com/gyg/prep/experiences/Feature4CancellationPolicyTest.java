package com.gyg.prep.experiences;

import com.gyg.prep.experiences.activity.ActivityRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

import static com.gyg.prep.experiences.TestData.bookingJson;
import static com.gyg.prep.experiences.TestData.startingIn;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * FEATURE 4: GetYourGuide-style free cancellation.
 *
 *  - Free cancellation up to 24 hours before the activity starts -> 200, status CANCELLED
 *  - Less than 24 hours before the start -> 409 {"error": "... 24 hours ..."}
 *  - Cancelling an already-cancelled booking -> 409
 *
 * Bonus discussion: how do you test the exact 24h boundary without sleeping? (Hint: inject java.time.Clock.)
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class Feature4CancellationPolicyTest {

    @Autowired MockMvc mvc;
    @Autowired ActivityRepository activities;

    Integer bookActivityStartingIn(Duration d) throws Exception {
        long activityId = activities.save(startingIn(d, 10)).getId();
        String body = mvc.perform(post("/bookings").contentType(MediaType.APPLICATION_JSON)
                        .content(bookingJson(activityId, 1)))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        return JsonPath.read(body, "$.id");
    }

    @Test
    void freeCancellationMoreThan24hBefore() throws Exception {
        Integer id = bookActivityStartingIn(Duration.ofDays(3));
        mvc.perform(post("/bookings/{id}/cancel", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    @Test
    void tooLateToCancel() throws Exception {
        Integer id = bookActivityStartingIn(Duration.ofHours(2));
        mvc.perform(post("/bookings/{id}/cancel", id))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value(containsString("24 hours")));
    }

    @Test
    void cannotCancelTwice() throws Exception {
        Integer id = bookActivityStartingIn(Duration.ofDays(3));
        mvc.perform(post("/bookings/{id}/cancel", id)).andExpect(status().isOk());
        mvc.perform(post("/bookings/{id}/cancel", id)).andExpect(status().isConflict());
    }
}
