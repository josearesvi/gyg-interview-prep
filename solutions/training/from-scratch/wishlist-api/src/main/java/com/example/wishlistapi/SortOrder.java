package com.example.wishlistapi;

import java.util.Comparator;

public enum SortOrder {
    PRICE_ASC(Comparator.comparing(WishlistItem::price)),
    PRICE_DESC(Comparator.comparing(WishlistItem::price).reversed());

    final Comparator<WishlistItem> comparator;

    SortOrder(Comparator<WishlistItem> comparator) {
        this.comparator = comparator;
    }

    static SortOrder parse(String value) {
        try {
            return valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ApiException.BadRequest("sort must be price_asc or price_desc");
        }
    }
}
