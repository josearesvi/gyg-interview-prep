package com.example.wishlistapi;

import java.math.BigDecimal;

public record WishlistSummary(int count, BigDecimal totalPrice, String currency) {
}
