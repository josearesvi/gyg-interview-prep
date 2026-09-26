package com.example.wishlistapi;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/** BigDecimal, never double, for money: 0.10 + 0.20 must be exactly 0.30. */
public record AddItemRequest(@NotNull Long activityId,
                             @NotBlank String title,
                             @NotBlank String city,
                             @NotNull @DecimalMin("0.00") @Digits(integer = 7, fraction = 2) BigDecimal price) {
}
