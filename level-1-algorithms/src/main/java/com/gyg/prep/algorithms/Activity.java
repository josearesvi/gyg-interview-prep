package com.gyg.prep.algorithms;

import java.math.BigDecimal;

/** A bookable experience, e.g. "Colosseum skip-the-line tour". Immutable. */
public record Activity(long id, String title, String city, double rating, int reviewCount, BigDecimal price) {
}
