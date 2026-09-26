package com.gyg.prep.bookings.booking;

import com.gyg.prep.bookings.activity.Activity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * FIXED.
 *  Bug 1: the discount was "10" (1000%) instead of 10%, so groups got negative totals.
 *  Bug 1b: money was computed in double (149.70000000000002). Use BigDecimal end to end and round once, at the end.
 */
@Service
public class PricingService {

    static final int GROUP_SIZE = 3;
    static final BigDecimal GROUP_DISCOUNT = new BigDecimal("0.10");

    public BigDecimal totalFor(Activity activity, int participants) {
        BigDecimal total = activity.getPricePerPerson().multiply(BigDecimal.valueOf(participants));
        if (participants >= GROUP_SIZE) {
            total = total.subtract(total.multiply(GROUP_DISCOUNT));
        }
        return total.setScale(2, RoundingMode.HALF_UP);
    }
}
