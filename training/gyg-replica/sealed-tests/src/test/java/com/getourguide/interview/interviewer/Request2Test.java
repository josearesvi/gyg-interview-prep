package com.getourguide.interview.interviewer;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

/** The interviewer's hidden checks for REQUEST-2. */
@SpringBootTest
@AutoConfigureMockMvc
class Request2Test {

    @Autowired
    MockMvc mvc;

    @Test
    void activitySearchIsCaseInsensitiveAndReturnsEveryMatch() throws Exception {
        mvc.perform(get("/activities/search/berlin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(10)))
                .andExpect(jsonPath("$[*].title", everyItem(containsStringIgnoringCase("berlin"))));
    }

    @Test
    void activitySearchWithoutMatchIsEmpty() throws Exception {
        mvc.perform(get("/activities/search/zzz-no-such-tour"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void supplierSearchReturnsAllMatchesCaseInsensitive() throws Exception {
        mvc.perform(get("/suppliers/search/berlin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name", containsInAnyOrder(
                        "Spree Tours GmbH", "City Pass Berlin", "Hauptstadt Guides", "Taste of Kreuzberg")));
    }

    @Test
    void supplierSearchWithoutMatchIsEmptyNotEverything() throws Exception {
        mvc.perform(get("/suppliers/search/atlantis"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
