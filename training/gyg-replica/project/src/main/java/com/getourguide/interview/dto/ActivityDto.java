package com.getourguide.interview.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActivityDto {
    private Long id;
    private String title;
    private int price;
    private String currency;
    private double rating;
    private boolean specialOffer;
    private String supplierName;
}
