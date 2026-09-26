package com.gyg.prep.bookings.booking;

import com.gyg.prep.bookings.activity.Activity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/** Groups of GROUP_SIZE or more people get GROUP_DISCOUNT_PERCENT off the total. */
@Service
public class PricingService {

    static final int GROUP_SIZE = 3;
    static final double GROUP_DISCOUNT_PERCENT = 10;

    public BigDecimal totalFor(Activity activity, int participants) {
        double total = activity.getPricePerPerson().doubleValue() * participants;
        if (participants >= GROUP_SIZE) {
            total = total - total * GROUP_DISCOUNT_PERCENT;
        }
        return BigDecimal.valueOf(total);
    }
}
