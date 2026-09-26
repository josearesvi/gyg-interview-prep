package com.gyg.prep.reviews;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

/** An injectable Clock makes "now" controllable in tests (Clock.fixed(...)). */
@Configuration
class TimeConfig {
    @Bean
    Clock clock() {
        return Clock.systemUTC();
    }
}
