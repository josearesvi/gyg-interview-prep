package com.example.wishlistapi;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/** Fast unit tests (no Spring). The black-box acceptance suite covers the HTTP layer. */
class WishlistServiceTest {

    private final WishlistService service =
            new WishlistService(Clock.fixed(Instant.parse("2026-10-01T10:00:00Z"), ZoneOffset.UTC));

    private static AddItemRequest item(long activityId, String price) {
        return new AddItemRequest(activityId, "Tour " + activityId, "Rome", new BigDecimal(price));
    }

    @Test
    void totalIsExact() {
        service.add("t", item(1, "0.10"));
        service.add("t", item(2, "0.20"));
        assertThat(service.summary("t").totalPrice()).isEqualByComparingTo("0.30");
    }

    @Test
    void rejectsDuplicatesAndTheTwentyFirstItem() {
        for (int i = 1; i <= WishlistService.MAX_ITEMS; i++) {
            service.add("t", item(i, "1.00"));
        }
        assertThatThrownBy(() -> service.add("t", item(1, "1.00"))).isInstanceOf(ApiException.Duplicate.class);
        assertThatThrownBy(() -> service.add("t", item(99, "1.00"))).isInstanceOf(ApiException.LimitReached.class);
    }

    @Test
    void addedAtComesFromTheClock() {
        assertThat(service.add("t", item(1, "5")).addedAt()).isEqualTo(Instant.parse("2026-10-01T10:00:00Z"));
    }
}
