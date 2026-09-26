package com.gyg.prep.reviews;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/** Lets ?sort=rating (lower case) bind to Reviews.SortOrder.RATING. */
@Component
class SortOrderConverter implements Converter<String, Reviews.SortOrder> {
    @Override
    public Reviews.SortOrder convert(String source) {
        return Reviews.SortOrder.from(source);
    }
}
