package com.getourguide.interview.dto;

import com.getourguide.interview.entity.Activity;
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

    /** The single mapping that used to be copy-pasted three times, with inconsistent null handling. */
    public static ActivityDto from(Activity activity) {
        return ActivityDto.builder()
                .id(activity.getId())
                .title(activity.getTitle())
                .price(activity.getPrice())
                .currency(activity.getCurrency())
                .rating(activity.getRating())
                .specialOffer(activity.isSpecialOffer())
                .supplierName(activity.getSupplier() == null ? "" : activity.getSupplier().getName())
                .build();
    }
}
