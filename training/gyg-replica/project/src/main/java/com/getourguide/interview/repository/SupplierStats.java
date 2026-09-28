package com.getourguide.interview.repository;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SupplierStats {
    private String supplierName;
    private Long activityCount;
    private Double totalRevenue;
    private Double averageRating;
}
