package com.gyg.prep.experiences;

import com.gyg.prep.experiences.activity.ActivityRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** What already works. These stay green while you build the features. */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class BaselineApiTest {

    @Autowired MockMvc mvc;
    @Autowired ActivityRepository activities;

    long activityId;

    @BeforeEach
    void setUp() {
        activityId = activities.save(activity("Colosseum", "Rome", "49.90", 4.7, 3)).getId();
    }

    ResultActions book(int participants) throws Exception {
        return mvc.perform(post("/bookings").contentType(MediaType.APPLICATION_JSON)
                .content(bookingJson(activityId, participants)));
    }

    @Test
    void getsAnActivity() throws Exception {
        mvc.perform(get("/activities/{id}", activityId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Colosseum"));
        mvc.perform(get("/activities/{id}", 987654)).andExpect(status().isNotFound());
    }

    @Test
    void booksAndRejectsWhenSoldOut() throws Exception {
        book(2).andExpect(status().isCreated())
                .andExpect(jsonPath("$.totalPrice").value(99.80))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
        book(2).andExpect(status().isConflict());
        book(1).andExpect(status().isCreated());
    }

    @Test
    void cancelsABooking() throws Exception {
        String body = book(3).andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.read(body, "$.id");

        mvc.perform(post("/bookings/{id}/cancel", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
        book(3).andExpect(status().isCreated());
    }

    @Test
    void validatesTheRequest() throws Exception {
        book(0).andExpect(status().isBadRequest());
        book(21).andExpect(status().isBadRequest());
    }
}
