package com.gyg.prep.experiences;

import com.gyg.prep.experiences.activity.ActivityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static com.gyg.prep.experiences.TestData.activity;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * FEATURE 1: search with filtering, pagination and sorting.
 *
 * GET /activities?city=rome&page=0&size=2&sort=price,asc
 *   -> {"content": [...], "page": 0, "size": 2, "totalElements": 3, "totalPages": 2}
 *
 *  - city: optional, case-insensitive, filtered IN THE DATABASE (not findAll() + stream)
 *  - page: default 0. size: default 20, capped at 100
 *  - sort: "price" | "rating" | "title", optional ",asc" / ",desc" (default asc). Anything else -> 400
 */
@Disabled("Feature 1: remove this line when you start")
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class Feature1PaginationTest {

    @Autowired MockMvc mvc;
    @Autowired ActivityRepository activities;

    @BeforeEach
    void setUp() {
        activities.deleteAll();
        activities.save(activity("Vatican", "Rome", "69.00", 4.8, 10));
        activities.save(activity("Trastevere food", "Rome", "89.00", 4.9, 10));
        activities.save(activity("Colosseum", "Rome", "49.90", 4.7, 10));
        activities.save(activity("Wall bike tour", "Berlin", "29.00", 4.6, 10));
    }

    @Test
    void filtersByCityAndPaginatesSortedByPrice() throws Exception {
        mvc.perform(get("/activities").param("city", "rome").param("size", "2").param("sort", "price"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].title").value("Colosseum"))
                .andExpect(jsonPath("$.content[1].title").value("Vatican"))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(2))
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.totalPages").value(2));

        mvc.perform(get("/activities").param("city", "ROME").param("size", "2").param("page", "1").param("sort", "price"))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].title").value("Trastevere food"));
    }

    @Test
    void sortsDescending() throws Exception {
        mvc.perform(get("/activities").param("sort", "rating,desc"))
                .andExpect(jsonPath("$.content[0].title").value("Trastevere food"))
                .andExpect(jsonPath("$.content[3].title").value("Wall bike tour"));
    }

    @Test
    void defaultsAndLimits() throws Exception {
        mvc.perform(get("/activities"))
                .andExpect(jsonPath("$.content", hasSize(4)))
                .andExpect(jsonPath("$.size").value(20));
        mvc.perform(get("/activities").param("size", "500"))
                .andExpect(jsonPath("$.size").value(100));
    }

    @Test
    void rejectsUnknownSortField() throws Exception {
        mvc.perform(get("/activities").param("sort", "supplierSecretMargin"))
                .andExpect(status().isBadRequest());
    }
}
