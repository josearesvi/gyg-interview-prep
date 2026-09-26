package com.example.wishlistapi;

import java.math.BigDecimal;
import java.time.Instant;

public record WishlistItem(long id, long activityId, String title, String city, BigDecimal price, Instant addedAt) {
}
