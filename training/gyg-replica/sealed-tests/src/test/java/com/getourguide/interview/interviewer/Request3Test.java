package com.getourguide.interview.interviewer;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

/** The interviewer's hidden checks for REQUEST-3. */
@SpringBootTest
@AutoConfigureMockMvc
class Request3Test {

    @Autowired
    MockMvc mvc;

    @Test
    void statsPerSupplierSortedByActivityCount() throws Exception {
        mvc.perform(get("/suppliers/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[0].supplierName").value("City Pass Berlin"))
                .andExpect(jsonPath("$[0].activityCount").value(5))
                .andExpect(jsonPath("$[0].totalRevenue").value(233))
                .andExpect(jsonPath("$[0].averageRating", closeTo(4.54, 0.001)));
    }

    @Test
    void statsForASmallerSupplier() throws Exception {
        mvc.perform(get("/suppliers/stats"))
                .andExpect(jsonPath("$[?(@.supplierName == 'Spree Tours GmbH')].activityCount").value(3))
                .andExpect(jsonPath("$[?(@.supplierName == 'Spree Tours GmbH')].totalRevenue").value(51));
    }
}
