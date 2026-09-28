package com.getourguide.interview.repository;

import java.util.Locale;

/** Builds a case-insensitive "contains" pattern where the user's % and _ are literal (escape char '!'). */
public final class LikePattern {

    private LikePattern() {
    }

    public static String contains(String userInput) {
        String escaped = userInput.toLowerCase(Locale.ROOT)
                .replace("!", "!!").replace("%", "!%").replace("_", "!_");
        return "%" + escaped + "%";
    }
}
