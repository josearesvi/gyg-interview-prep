package com.tourco.inventory.config;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "inventory")
public record InventoryProperties(@NotBlank String supplierApiKey, @Min(1) int maxSeatsPerReservation) {
}
