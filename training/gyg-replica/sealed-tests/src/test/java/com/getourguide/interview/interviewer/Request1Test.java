package com.getourguide.interview.interviewer;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

/** The interviewer's hidden checks for REQUEST-1. Copy into src/test/java/... when the walkthrough says so. */
@SpringBootTest
@AutoConfigureMockMvc
class Request1Test {

    @Autowired
    MockMvc mvc;

    @Test
    void existingActivity() throws Exception {
        mvc.perform(get("/activities/25651"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Reichstag: Parliament Quarter Tour & Glass Dome"))
                .andExpect(jsonPath("$.supplierName").value("Spree Tours GmbH"));
    }

    @Test
    void activityWhoseSupplierIsMissingStillLoads() throws Exception {
        mvc.perform(get("/activities/58820"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(58820))
                .andExpect(jsonPath("$.supplierName").value(""));
    }

    @Test
    void unknownActivityIs404() throws Exception {
        mvc.perform(get("/activities/999")).andExpect(status().isNotFound());
    }

    @Test
    void nonNumericIdIs400() throws Exception {
        mvc.perform(get("/activities/abc")).andExpect(status().isBadRequest());
    }

    @Test
    void supplierListIsFlatValidJson() throws Exception {
        mvc.perform(get("/suppliers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[0].activities").doesNotExist());
    }
}
