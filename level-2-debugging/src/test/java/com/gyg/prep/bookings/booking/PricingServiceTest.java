package com.gyg.prep.bookings.booking;

import com.gyg.prep.bookings.activity.Activity;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class PricingServiceTest {

    private final PricingService pricing = new PricingService();

    private static Activity pricedAt(String price) {
        return new Activity("t", "Rome", new BigDecimal(price), "EUR", 100, LocalDate.MIN, LocalDate.MAX);
    }

    @ParameterizedTest(name = "{1} x {0} = {2}")
    @CsvSource({
            "49.90, 1, 49.90",
            "49.90, 2, 99.80",
            "49.90, 3, 134.73",   // 149.70 - 10%
            "0.10,  3, 0.27",
            "19.99, 7, 125.94",   // 139.93 - 10% = 125.937 -> rounds HALF_UP to 125.94
    })
    void totalIsExactToTheCent(String price, int participants, String expected) {
        // isEqualTo (not isEqualByComparingTo): the scale matters too, since the API promises 2 decimals.
        assertThat(pricing.totalFor(pricedAt(price), participants)).isEqualTo(new BigDecimal(expected));
    }
}
