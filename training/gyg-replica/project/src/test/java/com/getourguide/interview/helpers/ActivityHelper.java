package com.getourguide.interview.helpers;

import com.getourguide.interview.entity.Activity;
import com.getourguide.interview.entity.Supplier;

public class ActivityHelper {

    public static Activity createActivity(Long id, String title, Supplier supplier) {
        Activity activity = new Activity();
        activity.setId(id);
        activity.setTitle(title);
        activity.setPrice(10);
        activity.setCurrency("$");
        activity.setRating(4.5);
        activity.setSpecialOffer(false);
        activity.setSupplier(supplier);
        return activity;
    }
}
