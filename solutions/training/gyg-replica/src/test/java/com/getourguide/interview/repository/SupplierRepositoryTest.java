package com.getourguide.interview.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.getourguide.interview.dto.SupplierStats;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

/**
 * JPA slice. By default @DataJpaTest swaps the file database for a fresh in-memory one, so no state leaks between
 * runs, and Flyway still applies the migrations to it.
 */
@DataJpaTest
class SupplierRepositoryTest {

    @Autowired
    SupplierRepository suppliers;

    @Test
    void statsAreComputedInTheDatabase() {
        SupplierStats top = suppliers.getSupplierStats().get(0);

        assertThat(top.supplierName()).isEqualTo("City Pass Berlin");
        assertThat(top.activityCount()).isEqualTo(5);
        assertThat(top.totalRevenue()).isEqualTo(233);
    }

    @Test
    void searchEscapesWildcards() {
        assertThat(suppliers.search(LikePattern.contains("%"))).isEmpty();
        assertThat(suppliers.search(LikePattern.contains("GERMANY"))).hasSize(4);
    }
}
