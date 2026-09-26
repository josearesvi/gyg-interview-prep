package com.gyg.prep.reviews;

import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Set;

/** REFACTORED. A business rule with a name, unit-testable without Spring or a database. */
@Component
public class ContentModeration {

    private static final Set<String> BLOCKED_WORDS = Set.of("scam", "fraud");

    public boolean shouldFlag(String comment) {
        String normalized = comment.toLowerCase(Locale.ROOT);
        return BLOCKED_WORDS.stream().anyMatch(normalized::contains);
    }
}
