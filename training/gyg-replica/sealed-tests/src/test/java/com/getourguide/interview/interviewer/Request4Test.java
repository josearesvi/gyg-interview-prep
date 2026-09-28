package com.getourguide.interview.interviewer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

/** The interviewer's hidden check for REQUEST-4: no N+1 on GET /activities. */
@SpringBootTest(properties = "spring.jpa.properties.hibernate.generate_statistics=true")
@AutoConfigureMockMvc
class Request4Test {

    @Autowired
    MockMvc mvc;

    @Autowired
    EntityManagerFactory entityManagerFactory;

    @Test
    void listingActivitiesDoesNotRunOneQueryPerSupplier() throws Exception {
        Statistics stats = entityManagerFactory.unwrap(SessionFactory.class).getStatistics();
        stats.clear();

        mvc.perform(get("/activities")).andExpect(status().isOk());

        assertThat(stats.getPrepareStatementCount())
                .as("SQL statements for GET /activities (17 activities, 5 suppliers)")
                .isLessThanOrEqualTo(2);
    }
}
