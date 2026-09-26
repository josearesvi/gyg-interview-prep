package com.tourco.inventory;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

final class Fixtures {

    static Instant local(String localDateTime, String zone) {
        return LocalDateTime.parse(localDateTime).atZone(ZoneId.of(zone)).toInstant();
    }

    private Fixtures() {
    }
}
