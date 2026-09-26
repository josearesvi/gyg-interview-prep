package com.gyg.prep.experiences;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

/** FEATURE 4: inject the clock so tests can pin "now" exactly at the 24h boundary. */
@Configuration
class TimeConfig {
    @Bean
    Clock clock() {
        return Clock.systemUTC();
    }
}
